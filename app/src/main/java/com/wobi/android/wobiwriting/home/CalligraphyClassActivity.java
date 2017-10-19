package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.ClassCourseDirectoryAdapter;
import com.wobi.android.wobiwriting.home.message.ShuFaKeTangMBRequest;
import com.wobi.android.wobiwriting.home.message.ShuFaKeTangResponse;
import com.wobi.android.wobiwriting.home.message.ShuFaKeTangYBRequest;
import com.wobi.android.wobiwriting.home.model.CalligraphyClassCourse;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.HomeItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CalligraphyClassActivity extends BaseVideoActivity{

    public static final String GRADE_ID = "grade_id";
    private static final String TAG = "CalligraphyClassActivity";
    private List<CalligraphyClassCourse> mDirectories =  new ArrayList<>();

    private int grade;
    private int term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);

        initDirectory();

        String grade_id = getIntent().getStringExtra(GRADE_ID);
        String type = getIntent().getStringExtra(HomeItemView.SUB_TYPE);
        LogUtil.d(TAG, "grade_id = "+grade_id+"  type == "+type);

        if (grade_id != null && !grade_id.isEmpty()){
            grade = Integer.parseInt(grade_id.substring(0,1));
            term =  Integer.parseInt(grade_id.substring(1,2));
            LogUtil.d(TAG, "grade = "+grade+"  term == "+term);
        }

        if (HomeItemView.SUB_TYPE_1.equals(type)){
            loadCalligraphyClassInfoForYB();
            adapter.setSelected(0);
        }else if (HomeItemView.SUB_TYPE_3.equals(type)){
            loadCalligraphyClassInfoForBrush();
            adapter.setSelected(1);
        }

        adapter.notifyDataSetChanged();
    }

    private void initData(){
        mTitles.add(getResources().getString(R.string.home_item_writing_class_hard));
        mTitles.add(getResources().getString(R.string.home_item_writing_class_brush));
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.home_item_writing_class;
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
    protected void onClickActionBarImageButton(){
        startSearchActivity(CALLIGRAGHY_CLASS);
    }

    @Override
    public void onItemClick(View view, int position) {
        adapter.setSelected(position);
        adapter.notifyDataSetChanged();
        if (position == 0){
            loadCalligraphyClassInfoForYB();
        }else if (position == 1){
            loadCalligraphyClassInfoForBrush();
        }
    }

    protected void initDirectory() {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_directory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 4, 9));
        mAdapter = new ClassCourseDirectoryAdapter(getApplicationContext(),mDirectories);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ClassCourseDirectoryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.d(TAG," position == "+position);
                if (SharedPrefUtil.getLoginInfo(getApplicationContext()).isEmpty()){
                    if (position == 0){
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                        play(mDirectories.get(position).getVideoUrl());
                    }else {
                        checkLogin();
                    }
                }else {
                    mAdapter.setSelected(position);
                    mAdapter.notifyDataSetChanged();
                    play(mDirectories.get(position).getVideoUrl());
                }
            }
        });
    }

    private void loadCalligraphyClassInfoForBrush(){
        ShuFaKeTangMBRequest request = new ShuFaKeTangMBRequest();
        request.setGradeId(grade);
        request.setTermNum(term);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                ShuFaKeTangResponse shuFaKeTangResponse = gson.fromJson(response, ShuFaKeTangResponse.class);
                if (shuFaKeTangResponse.getSfktList() != null
                        && !shuFaKeTangResponse.getSfktList().isEmpty()){
                    mDirectories.clear();
                    mDirectories.addAll(shuFaKeTangResponse.getSfktList());
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }

    private void loadCalligraphyClassInfoForYB(){
        ShuFaKeTangYBRequest request = new ShuFaKeTangYBRequest();
        request.setGradeId(grade);
        request.setTermNum(term);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                ShuFaKeTangResponse shuFaKeTangResponse = gson.fromJson(response, ShuFaKeTangResponse.class);
                if (shuFaKeTangResponse.getSfktList() != null
                        && !shuFaKeTangResponse.getSfktList().isEmpty()){
                    mDirectories.clear();
                    mDirectories.addAll(shuFaKeTangResponse.getSfktList());
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }
}
