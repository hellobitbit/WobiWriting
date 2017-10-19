package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNActivity extends ActionBarActivity
        implements SpeakTypeAdapter.OnRecyclerViewItemClickListener, SpeakSZAdapter.OnRecyclerViewItemClickListener{
    public static final String GRADE_ID = "grade_id";
    private static final String TAG = "SpeakCNActivity";
    private List<KeWenDirectory> mDirectories =  new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private List<String> szList = new ArrayList<>();
    private Map<String, GetSZInfoResponse> szInfoResponseMap = new HashMap<>();
    private String grade_id;
    private RecyclerView mSpeakTypeRecyclerView;
    private SpeakTypeAdapter mAdapter;
    private RecyclerView szListRecycler;
    private SpeakSZAdapter mSZAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_cn_layout);
        grade_id = getIntent().getStringExtra(GRADE_ID);
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
        Intent kewenDirIntent = new Intent(SpeakCNActivity.this, KewenDirectoryActivity.class);
        kewenDirIntent.putExtra(KewenDirectoryActivity.GRADE_ID, grade_id);
        startActivity(kewenDirIntent);
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
                    if (!getKWMLListResponse.getKwmlList().isEmpty()){
                        mDirectories.clear();
                        mDirectories.addAll(getKWMLListResponse.getKwmlList());
                        updateActivityTitle();
                    }else {
                        showErrorMsg("获取课文目录数据异常");
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
        updateTitleText(mDirectories.get(2).getKewen());
        loadSZList();
    }

    private void loadSZList(){
        GetSZListRequest request = new GetSZListRequest();
        request.setJcId(mJCList.get(0).getId());
        request.setKwUrl(mDirectories.get(2).getKwUrl());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZListResponse getSZListResponse = gson.fromJson(response, GetSZListResponse.class);
                if (getSZListResponse != null && getSZListResponse.getHandleResult().equals("OK")){
                    if (!getSZListResponse.getSzList().isEmpty()) {
                        szList.clear();
                        szList.addAll(getSZListResponse.getSzList());
                        szInfoResponseMap.clear();
                        mSZAdapter.notifyDataSetChanged();

                        loadSZInfo(getSZListResponse.getSzList().get(0));
                    }else {
                        showErrorMsg("获取生字列表数据异常");
                    }
                }else {
                    showErrorMsg("获取生字列表数据异常");
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

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.speakType){
            LogUtil.d(TAG," onItemClick speakType");
            mAdapter.setSelected(position);
            mAdapter.notifyDataSetChanged();
        }else if (view.getId() == R.id.speakSz){
            LogUtil.d(TAG," onItemClick speakSz");
            mSZAdapter.setSelected(position);
            mSZAdapter.notifyDataSetChanged();
            if (szInfoResponseMap.containsKey(szList.get(position))){
                updateSzRelatedUI(szInfoResponseMap.get(szList.get(position)));
            }else {
                loadSZInfo(szList.get(position));
            }
        }
    }
}
