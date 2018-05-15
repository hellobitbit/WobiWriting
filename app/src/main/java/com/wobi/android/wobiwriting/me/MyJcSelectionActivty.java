package com.wobi.android.wobiwriting.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.adapters.JcListAdapter;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2018/5/12.
 */

public class MyJcSelectionActivty extends ActionBarActivity implements JcListAdapter.OnRecyclerViewItemClickListener{

    private static final String TAG = "MyJcSelectionActivty";
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JcListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_selection_layout);
        initViews();
        setCustomActionBar();
        updateTitleText("我的教材");
        loadJCList();
    }

    private void initViews() {

        recyclerView = (RecyclerView)findViewById(R.id.recycler_jc_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 0, 9));

        mAdapter = new JcListAdapter(getApplicationContext(),mJCList);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    private void loadJCList(){
        GetJCListRequest request = new GetJCListRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetJCListResponse getJCListResponse = gson.fromJson(response, GetJCListResponse.class);
                if (getJCListResponse!=null && getJCListResponse.getHandleResult().equals("OK")){
                    mJCList.clear();
                    mJCList.addAll(getJCListResponse.getJcList());
                    mAdapter.notifyDataSetChanged();
                }else {
                    showErrorMsg("获取教程失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
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
        SharedPrefUtil.setJC_ID(getApplicationContext(),
                mJCList.get(position).getId());
        mAdapter.notifyDataSetChanged();
    }
}
