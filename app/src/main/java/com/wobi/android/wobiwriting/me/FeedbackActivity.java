package com.wobi.android.wobiwriting.me;

import android.os.Bundle;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class FeedbackActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_layout);
        initViews();
        setCustomActionBar();
    }

    private void initViews(){

    }

    @Override
    public void onClickActionBarTextView(){
        //bussiness
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_feedback_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return R.string.action_bar_right_title_send;
    }
}
