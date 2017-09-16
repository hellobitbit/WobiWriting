package com.wobi.android.wobiwriting.me;

import android.os.Bundle;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.CustomActionBarActivity;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyMomentActivity extends CustomActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment_layout);
        initViews();

    }

    private void initViews(){

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_my_moment_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }
}
