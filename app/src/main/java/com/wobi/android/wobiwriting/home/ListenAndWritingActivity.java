package com.wobi.android.wobiwriting.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.ListenSZAdapter;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.home.model.ListenSerializableMap;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/21.
 */

public class ListenAndWritingActivity extends ActionBarActivity
        implements ListenSZAdapter.OnRecyclerViewItemClickListener{

    public static final String KEWEN_DATA = "kewen_data";
    private static final String TAG = "ListenAndWritingActivity";
    private ListenSerializableMap kewenDataMap;
    private ListenSZAdapter listenSZAdapter;
    private HashMap<String, GetSZInfoResponse> szInfoResponseHashMap;

    private final List<GetSZInfoResponse> szInfoResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_writing_layout);
        Bundle bundle = getIntent().getExtras();
        kewenDataMap = (ListenSerializableMap) bundle.get(KEWEN_DATA);
        initViews();
        setCustomActionBar();
        updateImageResource(R.drawable.listen_writing_exit);
        if (kewenDataMap != null) {
            szInfoResponseHashMap =  kewenDataMap.getSzInfoResponseMap();
            updateTitleText(kewenDataMap.getTitle());
            initData();
        }
    }

    private void initData(){
        for (String sz: kewenDataMap.getSzList()){
            if (szInfoResponseHashMap.containsKey(sz)){
                szInfoResponses.add(szInfoResponseHashMap.get(sz));
            }else {
                loadSZInfo(sz);
            }
        }
    }

    private void initViews() {
        RecyclerView listenSZListRecycler = (RecyclerView)findViewById(R.id.listenSZListRecycler);
        listenSZAdapter = new ListenSZAdapter(this, szInfoResponses);
        listenSZAdapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listenSZListRecycler.setLayoutManager(layoutManager);
        listenSZListRecycler.setHasFixedSize(true);
        listenSZListRecycler.setAdapter(listenSZAdapter);
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return 0;
    }

    @Override
    public void onItemClick(View view, int position) {

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
                    szInfoResponseHashMap.put(text, szInfoResponse);
                    updateSZDisplay(text, szInfoResponse);
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

    private void updateSZDisplay(String text, GetSZInfoResponse szInfoResponse){
        int position = kewenDataMap.getSzList().indexOf(text);
        if ((position)> szInfoResponses.size()-1){
            szInfoResponses.add(szInfoResponse);
        }else {
            szInfoResponses.add(position,szInfoResponse);
        }
        listenSZAdapter.notifyDataSetChanged();
    }
}
