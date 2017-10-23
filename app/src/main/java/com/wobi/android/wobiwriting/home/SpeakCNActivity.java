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
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.home.model.ListenSerializableMap;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNActivity extends ActionBarActivity
        implements SpeakTypeAdapter.OnRecyclerViewItemClickListener, SpeakSZAdapter.OnRecyclerViewItemClickListener{
    public static final String GRADE_ID = "grade_id";
    public static final String SPEAK_TYPE ="speak_type";
    public static final String SZ_LIST ="sz_list";
    public static final String KEWEN_TITLE ="kewen_title";
    private static final String TAG = "SpeakCNActivity";
    private static final String VIDEO_SUFFIX = ".mp4";
    private ArrayList<String> szList = new ArrayList<>();
    private HashMap<String, GetSZInfoResponse> szInfoResponseMap = new HashMap<>();
    private RecyclerView mSpeakTypeRecyclerView;
    private SpeakTypeAdapter mAdapter;
    private RecyclerView szListRecycler;
    private SpeakSZAdapter mSZAdapter;
    private int speakTypeValue = 0;
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
        speakTypeValue = getIntent().getIntExtra(SPEAK_TYPE, 0);
        szList = getIntent().getStringArrayListExtra(SZ_LIST);
        String kewenTitle = getIntent().getStringExtra(KEWEN_TITLE);
        initViews();
        setCustomActionBar();
        updateTitleText(kewenTitle);

        int szPosition = SharedPrefUtil.getSZPosition(getApplicationContext());
        switchSz(szList.size()> szPosition ? szPosition : 0);
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
                Intent intent = new Intent(getApplicationContext(), ListenActivity.class);
                ListenSerializableMap map =  new ListenSerializableMap();
                map.setTitle(getTitleText());
                map.setSzList(szList);
                map.setSzInfoResponseMap(szInfoResponseMap);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ListenActivity.KEWEN_DATA, map);
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
