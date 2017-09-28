package com.wobi.android.wobiwriting.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.CustomActionBarActivity;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CNClassicActivity extends BaseVideoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);

    }

    private void initData(){
        mTitles.add(getResources().getString(R.string.home_item_classic_dizigui));
        mTitles.add(getResources().getString(R.string.home_item_classic_three));
        mTitles.add(getResources().getString(R.string.home_item_classic_daode));
        mTitles.add(getResources().getString(R.string.home_item_classic_ancient_poetry));
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.home_item_classic;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
