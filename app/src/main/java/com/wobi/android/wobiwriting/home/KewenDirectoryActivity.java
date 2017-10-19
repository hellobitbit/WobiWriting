package com.wobi.android.wobiwriting.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.KewenDirectoryAdapter;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.message.GetKWMLListRequest;
import com.wobi.android.wobiwriting.home.message.GetKWMLListResponse;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/5.
 */

public class KewenDirectoryActivity extends BaseActivity {
    public static final String GRADE_ID = "grade_id";
    private static final String TAG = "KewenDirectoryActivity";
    private List<KeWenDirectory> mDirectories =  new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private KewenDirectoryAdapter mAdapter;
    private String grade_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kewen_directory_layout);
        grade_id = getIntent().getStringExtra(GRADE_ID);
        loadJCList();
        initViews();

    }

    private void initViews() {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_kewen_directory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 4, 9));

        mAdapter = new KewenDirectoryAdapter(getApplicationContext(),mDirectories);
        recyclerView.setAdapter(mAdapter);
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
                if (!getKWMLListResponse.getKwmlList().isEmpty()){
                    mDirectories.clear();
                    mDirectories.addAll(getKWMLListResponse.getKwmlList());
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }

    private void loadJCList(){
        GetJCListRequest request = new GetJCListRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetJCListResponse getJCListResponse = gson.fromJson(response, GetJCListResponse.class);
                mJCList.clear();
                mJCList.addAll(getJCListResponse.getJcList());
                loadKWMLList(mJCList.get(0).getId());
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }
}
