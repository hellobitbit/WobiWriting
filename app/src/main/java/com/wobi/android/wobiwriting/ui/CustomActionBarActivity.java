package com.wobi.android.wobiwriting.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/9/13.
 */

public abstract class CustomActionBarActivity extends AppCompatActivity {

    private View mActionbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbar();
    }

    private void setActionbar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionbarLayout = LayoutInflater.from(this).inflate(
                    R.layout.actionbar_layout, null);
            actionBar.setCustomView(mActionbarLayout);
            ImageButton back = (ImageButton) mActionbarLayout.findViewById(R.id.actionbar_left_back);
            TextView title = (TextView) mActionbarLayout.findViewById(R.id.actionbar_title);
            if (getActionBarTitle() > 0){
                title.setText(getActionBarTitle());
            }else {
                title.setText("");
            }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            ImageButton rightButton = (ImageButton) mActionbarLayout.findViewById(R.id.actionbar_right_button);
            TextView rightTitle = (TextView) mActionbarLayout.findViewById(R.id.actionbar_right_title);
            if (getActionBarRightButtonRes() == -1){
                rightButton.setVisibility(View.INVISIBLE);
            }else {
                rightButton.setVisibility(View.VISIBLE);
                rightButton.setImageResource(getActionBarRightButtonRes());
            }

            if (getActionBarRightTitleRes() == -1){
                rightTitle.setVisibility(View.INVISIBLE);
            }else {
                rightTitle.setVisibility(View.VISIBLE);
                rightTitle.setText(getActionBarRightTitleRes());
            }

            rightTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickActionBarTextView();
                }
            });

            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickActionBarImageButton();
                }
            });
        }
    }

    protected abstract int getActionBarTitle();

    protected abstract int getActionBarRightButtonRes();

    protected abstract int getActionBarRightTitleRes();

    protected void onClickActionBarTextView(){
        //subclass impl if needs
    }

    protected void onClickActionBarImageButton(){
        //subclass impl if needs

    }

}
