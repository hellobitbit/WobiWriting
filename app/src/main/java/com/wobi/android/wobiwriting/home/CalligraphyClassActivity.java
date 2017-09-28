package com.wobi.android.wobiwriting.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CalligraphyClassActivity extends BaseVideoActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);

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
}
