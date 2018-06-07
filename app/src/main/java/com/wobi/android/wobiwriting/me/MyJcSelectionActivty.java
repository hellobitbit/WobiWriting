package com.wobi.android.wobiwriting.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.ui.MainActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.user.adapters.JcListAdapter;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2018/5/12.
 */

public class MyJcSelectionActivty extends ActionBarActivity implements JcListAdapter.OnRecyclerViewItemClickListener{

    private static final String TAG = "MyJcSelectionActivty";
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private List<JiaoCaiObject> mZxJCList = new ArrayList<>();
    private List<JiaoCaiObject> mAllJCList = new ArrayList<>();
    private RecyclerView recyclerView, zx_recyclerView;
    private JcListAdapter mAdapter,mZxAdapter;
    private int mLastXxSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_selection_layout);
        initViews();
        setCustomActionBar();
        updateTitleText("我的教材");
        loadJCList();
        updateBackVisibility(View.GONE);
    }

    private void initViews() {

        recyclerView = (RecyclerView)findViewById(R.id.recycler_jc_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new JcListAdapter(getApplicationContext(),mJCList, recyclerView);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        zx_recyclerView = (RecyclerView)findViewById(R.id.zx_recycler_jc_list);
        zx_recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager zx_layoutManager = new LinearLayoutManager(getApplicationContext());
        zx_recyclerView.setLayoutManager(zx_layoutManager);

        mZxAdapter = new JcListAdapter(getApplicationContext(),mZxJCList, zx_recyclerView);
        mZxAdapter.setOnItemClickListener(this);
        zx_recyclerView.setAdapter(mZxAdapter);
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

                    mAllJCList.clear();
                    mAllJCList.addAll(getJCListResponse.getJcList());

                    mJCList.clear();
                    mJCList.addAll(getXiaoxueJcList(mAllJCList));
                    mAdapter.setJcId(SharedPrefUtil.getXX_JC_ID(getApplicationContext()));
                    mAdapter.notifyDataSetChanged();

                    mZxJCList.clear();
                    mZxJCList.addAll(getZxJcList(mAllJCList));

                    updateXxZxRelationship(getSort(SharedPrefUtil.getXX_JC_ID(getApplicationContext())));
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
    public void onClickActionBarTextView(){
        //bussiness
        if (mZxAdapter.getJcId() == -1 || mAdapter.getJcId() == -1){
            Toast.makeText(getApplicationContext()," 小学教材与中学教学必须选择",Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefUtil.updateFiveYearsVersion(getApplicationContext(), isFiveYearsVersion(mAdapter.getJcId()));
        SharedPrefUtil.setXX_JC_ID(getApplicationContext(), mAdapter.getJcId());
        SharedPrefUtil.setZX_JC_ID(getApplicationContext(), mZxAdapter.getJcId());
        finish();
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
        return R.string.action_bar_right_title_confirm;
    }

    @Override
    public void onItemClick(View parentView, View view, int position) {
        if(parentView.getId() == R.id.recycler_jc_list){
            mAdapter.setJcId(mJCList.get(position).getId());
            mAdapter.notifyDataSetChanged();
            updateXxZxRelationship(mJCList.get(position).getSort());
        }else if (parentView.getId() == R.id.zx_recycler_jc_list){
            mZxAdapter.setJcId(mZxJCList.get(position).getId());
            mZxAdapter.notifyDataSetChanged();
        }
    }

    private boolean isFiveYearsVersion(int jc_id){
        for (JiaoCaiObject obj : mAllJCList){
            if (obj.getId() == jc_id){
                return obj.getSort() == 10;
            }
        }
        return false;
    }

    private List<JiaoCaiObject> getXiaoxueJcList(List<JiaoCaiObject> mJCList){
        List<JiaoCaiObject> list = new ArrayList<>();
        for (JiaoCaiObject obj : mJCList){
            if (obj.getEduSchool() == 1){
                list.add(obj);
            }
        }
        return list;
    }

    private List<JiaoCaiObject> getZxJcList(List<JiaoCaiObject> mJCList){
        List<JiaoCaiObject> list = new ArrayList<>();
        for (JiaoCaiObject obj : mJCList){
            if (obj.getEduSchool() == 2){
                list.add(obj);
            }
        }
        return list;
    }

    private List<JiaoCaiObject> getFilterZxJcList(boolean isXxFive){
        List<JiaoCaiObject> list = new ArrayList<>();
        for (JiaoCaiObject obj : mAllJCList){
            if (obj.getEduSchool() == 2) {
                if (isXxFive) {
                    if (obj.getSort() == 4) {
                        list.add(obj);
                    }
                } else {
                    if (obj.getSort() != 4) {
                        list.add(obj);
                    }
                }
            }
        }
        return list;
    }

    private void updateXxZxRelationship(int sort){
        if (sort != mLastXxSort){
            if (sort == 10){
                mZxJCList.clear();
                mZxJCList.addAll(getFilterZxJcList(true));
            }else {
                mZxJCList.clear();
                mZxJCList.addAll(getFilterZxJcList(false));
            }
            if (mZxJCList.size() == 1) {
                mZxAdapter.setJcId(mZxJCList.get(0).getId());
            }else {
                mZxAdapter.setJcId(SharedPrefUtil.getZX_JC_ID(getApplicationContext()));
            }
            mZxAdapter.notifyDataSetChanged();
            mLastXxSort = sort;
        }
    }

    private int getSort(int jc_id){
        for (JiaoCaiObject obj : mJCList){
            if (obj.getId() == jc_id){
                return obj.getSort();
            }
        }
        return -1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}
