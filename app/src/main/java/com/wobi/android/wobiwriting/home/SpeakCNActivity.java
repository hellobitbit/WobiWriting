package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.SpeakSZAdapter;
import com.wobi.android.wobiwriting.home.adapters.SpeakTypeAdapter;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.message.GetKWMLListRequest;
import com.wobi.android.wobiwriting.home.message.GetKWMLListResponse;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.home.message.GetSZListRequest;
import com.wobi.android.wobiwriting.home.message.GetSZListResponse;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;
import com.wobi.android.wobiwriting.home.model.ListenSerializableMap;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNActivity extends ActionBarActivity
        implements SpeakTypeAdapter.OnRecyclerViewItemClickListener, SpeakSZAdapter.OnRecyclerViewItemClickListener{
    public static final String GRADE_ID = "grade_id";
    public static final String SPEAK_TYPE ="speak_type";
    private static final String TAG = "SpeakCNActivity";
    private static final String VIDEO_SUFFIX = ".mp4";
    private static final int REQUEST_CODE = 1007;
    private List<KeWenDirectory> mDirectories =  new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private ArrayList<String> szList = new ArrayList<>();
    private HashMap<String, List<String>> szListMap = new HashMap<>();
    private HashMap<String, GetSZInfoResponse> szInfoResponseMap = new HashMap<>();
    private String grade_id;
    private RecyclerView mSpeakTypeRecyclerView;
    private SpeakTypeAdapter mAdapter;
    private RecyclerView szListRecycler;
    private SpeakSZAdapter mSZAdapter;
    private int speakTypeValue = 0;

    private int kewenPosition;
    private VideoView speak_videoView;

    private GetSZInfoResponse currentSzInfo;

    public enum SpeakType{
        SWJZ(0),
        BISHUN(1),
        BANSHU(2),
        YINGBI(3),
        MAOBI(4);

        private int mValue;

        SpeakType(int value){
            mValue = value;
        }

        public int getValue(){
            return mValue;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_cn_layout);
        grade_id = getIntent().getStringExtra(GRADE_ID);
        speakTypeValue = getIntent().getIntExtra(SPEAK_TYPE, 0);
        kewenPosition = SharedPrefUtil.getKewenDirectoryPosition(getApplicationContext());
        loadJCList();
        initViews();
        setCustomActionBar();
    }

    private void initViews() {
        //得到控件
        mSpeakTypeRecyclerView = (RecyclerView) findViewById(R.id.speakTypeRecycler);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSpeakTypeRecyclerView.setLayoutManager(linearLayoutManager);
        mSpeakTypeRecyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 30, 0));
        mSpeakTypeRecyclerView.setHasFixedSize(true);
        //设置适配器
        mAdapter = new SpeakTypeAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setSelected(speakTypeValue);
        mSpeakTypeRecyclerView.smoothScrollToPosition(speakTypeValue);
        mSpeakTypeRecyclerView.setAdapter(mAdapter);


        //得到控件
        szListRecycler = (RecyclerView) findViewById(R.id.szListRecycler);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        szListRecycler.setLayoutManager(linearLayoutManager1);
        szListRecycler.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 13, 0));
        szListRecycler.setHasFixedSize(true);
        //设置适配器
        mSZAdapter = new SpeakSZAdapter(this,szList);
        mSZAdapter.setOnItemClickListener(this);
        szListRecycler.setAdapter(mSZAdapter);

        ImageView listen = (ImageView)findViewById(R.id.listenAndWriting);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListenAndWritingActivity.class);
                ListenSerializableMap map =  new ListenSerializableMap();
                map.setTitle(getTitleText());
                map.setSzList(szList);
                map.setSzInfoResponseMap(szInfoResponseMap);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ListenAndWritingActivity.KEWEN_DATA, map);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        speak_videoView = (VideoView)findViewById(R.id.speak_videoView);
        speak_videoView.setMediaController(new MediaController(this));
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return R.drawable.search_normal;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    @Override
    protected void onClickActionBarTitle(){
        if (!getTitleText().isEmpty()) {
            Intent intent = new Intent(SpeakCNActivity.this, KewenDirectoryActivity.class);
            intent.putExtra(KewenDirectoryActivity.GRADE_ID, grade_id);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void loadJCList(){
        GetJCListRequest request = new GetJCListRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetJCListResponse getJCListResponse = gson.fromJson(response, GetJCListResponse.class);
                if (getJCListResponse != null && getJCListResponse.getHandleResult().equals("OK")) {
                    if (!getJCListResponse.getJcList().isEmpty()){
                        mJCList.clear();
                        mJCList.addAll(getJCListResponse.getJcList());
                        loadKWMLList(mJCList.get(0).getId());
                    }else {
                        showErrorMsg("获取教程数据异常");
                    }

                }else {
                    showErrorMsg("获取教程数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void loadKWMLList(int jcId){
        GetKWMLListRequest request = new GetKWMLListRequest();
        request.setGradeId(grade_id);
        request.setJcId(jcId);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetKWMLListResponse getKWMLListResponse = gson.fromJson(response, GetKWMLListResponse.class);
                if (getKWMLListResponse != null && getKWMLListResponse.getHandleResult().equals("OK")){
                    if (getKWMLListResponse.getKwmlList() != null
                            && !getKWMLListResponse.getKwmlList().isEmpty()){
                        mDirectories.clear();
                        mDirectories.addAll(getKWMLListResponse.getKwmlList());
                        Iterator<KeWenDirectory> it = mDirectories.iterator();
                        while (it.hasNext()) {
                            KeWenDirectory s = it.next();
                            if (s.getKwUrl().equals("")) {
                                it.remove();
                            }
                        }
                        updateActivityTitle();
                    }else {
                        showErrorMsg("该教程目前没有课文哦");
                    }
                }else {
                    showErrorMsg("获取课文目录数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void updateActivityTitle(){
        updateTitleText(mDirectories.get(kewenPosition).getKewen());
        loadSZList(mDirectories.get(kewenPosition).getKwUrl());
    }

    private void loadSZList(final String kwUrl){
        GetSZListRequest request = new GetSZListRequest();
        request.setJcId(mJCList.get(0).getId());
        request.setKwUrl(kwUrl);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZListResponse getSZListResponse = gson.fromJson(response, GetSZListResponse.class);
                if (getSZListResponse != null && getSZListResponse.getHandleResult().equals("OK")){
                    LogUtil.d(TAG,"loadSZList  = "+getSZListResponse.getSzList());
                    if (getSZListResponse.getSzList() != null
                            && !getSZListResponse.getSzList().isEmpty()) {
                        szList.clear();
                        szList.addAll(getSZListResponse.getSzList());
                        szListMap.put(kwUrl,getSZListResponse.getSzList());

                        int szPosition = SharedPrefUtil.getSZPosition(getApplicationContext());
                        switchSz(szList.size()> szPosition ? szPosition : 0);
                    }else {
                        showErrorMsg("该课文暂没生字哦");
                    }
                }else {
                    showErrorMsg("获取课文生字异常");
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void loadSZInfo(final String text){
        GetSZInfoRequest request = new GetSZInfoRequest();
        request.setSz(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZInfoResponse szInfoResponse = gson.fromJson(response, GetSZInfoResponse.class);
                if (szInfoResponse != null && szInfoResponse.getHandleResult().equals("OK")){
                    szInfoResponseMap.put(text, szInfoResponse);
                    currentSzInfo = szInfoResponse;
                    updateSzRelatedUI(szInfoResponse);
                }else {
                    showErrorMsg("获取生字信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void updateSzRelatedUI(GetSZInfoResponse szInfoResponse){
        ((TextView)findViewById(R.id.pinyin_content)).setText(szInfoResponse.getPinyin());
        ((TextView)findViewById(R.id.sz_content)).setText(szInfoResponse.getWord());
        ((TextView)findViewById(R.id.bushou_content)).setText(szInfoResponse.getBushou()+"部");
        ((TextView)findViewById(R.id.zxjg_content)).setText(szInfoResponse.getZxjg());
        ((TextView)findViewById(R.id.zuci_content)).setText(szInfoResponse.getZuci());

        refreshVideoPlay(szInfoResponse);
    }

    private void refreshVideoPlay(GetSZInfoResponse szInfoResponse){
        //play media
        if (speakTypeValue == SpeakType.SWJZ.getValue()){
            String url = szInfoResponse.getSwjz_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.BISHUN.getValue()){
            String url = szInfoResponse.getBishun_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.BANSHU.getValue()){
            String url = szInfoResponse.getBanshu_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.YINGBI.getValue()){
            String url = szInfoResponse.getYingbi_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.MAOBI.getValue()){
            String url = szInfoResponse.getMaobi_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }
    }

    private void play(String url){
        LogUtil.d(TAG," play url = "+url);
        speak_videoView.setVideoURI(Uri.parse(url));
        speak_videoView.start();
        speak_videoView.requestFocus();

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.speakType){
            mAdapter.setSelected(position);
            updateSpeakType(position);
            mAdapter.notifyDataSetChanged();
            if (currentSzInfo != null) {
                refreshVideoPlay(currentSzInfo);
            }
        }else if (view.getId() == R.id.speakSz){
            switchSz(position);
        }
    }

    @Override
    protected void onClickActionBarImageButton(){
        startSearchActivity(1);
    }

    private void startSearchActivity(int type){
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    private void updateSpeakType(int position){
        speakTypeValue = position;
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == REQUEST_CODE){
            int kewenPosition = SharedPrefUtil.getKewenDirectoryPosition(getApplicationContext());
            if (mDirectories.size() > 0 && kewenPosition < mDirectories.size()){
                updateTitleText(mDirectories.get(kewenPosition).getKewen());
                String kwUrl = mDirectories.get(kewenPosition).getKwUrl();
                if (szListMap.containsKey(kwUrl)){
                    szList = (ArrayList<String>) szListMap.get(kwUrl);
                    int szPosition = SharedPrefUtil.getSZPosition(getApplicationContext());
                    switchSz(szList.size()> szPosition ? szPosition : 0);
                }else {
                    loadSZList(mDirectories.get(kewenPosition).getKwUrl());
                }
            }else{
                loadJCList();
            }
        }
    }

    private void switchSz(int position){
        mSZAdapter.setSelected(position);
        mSZAdapter.notifyDataSetChanged();
        if (szInfoResponseMap.containsKey(szList.get(position))){
            updateSzRelatedUI(szInfoResponseMap.get(szList.get(position)));
            currentSzInfo = szInfoResponseMap.get(szList.get(position));
        }else {
            loadSZInfo(szList.get(position));
        }
    }
}
