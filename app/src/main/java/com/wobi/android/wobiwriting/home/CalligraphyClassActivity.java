package com.wobi.android.wobiwriting.home;

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
import com.wobi.android.wobiwriting.home.model.CalligraphyClassCourse;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CalligraphyClassActivity extends BaseVideoActivity{

    private static final String TAG = "CalligraphyClassActivity";
    private List<CalligraphyClassCourse> mDirectories =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);

        initDirectory();

        loadCalligraphyClassInfoForBrush();
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
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    @Override
    public void onItemClick(View view, int position) {

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

            }
        });
    }

    private void loadCalligraphyClassInfoForBrush(){
        ShuFaKeTangMBRequest request = new ShuFaKeTangMBRequest();
        request.setGradeId(1);
        request.setTermNum(1);
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
