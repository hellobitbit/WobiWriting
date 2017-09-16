package com.wobi.android.wobiwriting.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyFollowActivity extends AppCompatActivity {

    private View mActionbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow_layout);
        setActionbar();
        initViews();

    }

    private void initViews(){

    }

    private void setActionbar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionbarLayout = LayoutInflater.from(this).inflate(
                    R.layout.actionbar_my_follow_layout, null);
            actionBar.setCustomView(mActionbarLayout);
            ImageButton back = (ImageButton) mActionbarLayout.findViewById(R.id.actionbar_left_back);
            TextView title = (TextView) mActionbarLayout.findViewById(R.id.actionbar_title);
        }
    }
}
