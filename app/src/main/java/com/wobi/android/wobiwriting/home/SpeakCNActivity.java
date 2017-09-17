package com.wobi.android.wobiwriting.home;

import android.os.Bundle;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.CustomActionBarActivity;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNActivity extends CustomActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_cn_layout);
        initViews();

    }

    private void initViews() {

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.home;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }
}
