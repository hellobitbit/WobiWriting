package com.wobi.android.wobiwriting.home;

import android.os.Bundle;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_cn_layout);
        initViews();
        setCustomActionBar();
    }

    private void initViews() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetDataManager.getInstance();
            }
        }).start();

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
