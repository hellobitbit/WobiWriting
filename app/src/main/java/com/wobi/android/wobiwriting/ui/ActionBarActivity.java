package com.wobi.android.wobiwriting.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/10/16.
 */

public abstract class ActionBarActivity extends BaseActivity {

    private TextView title;
    private ImageButton back;

    public void setCustomActionBar(){
        back = (ImageButton) findViewById(R.id.actionbar_left_back);
        title = (TextView) findViewById(R.id.actionbar_title);
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
        ImageButton rightButton = (ImageButton) findViewById(R.id.actionbar_right_button);
        TextView rightTitle = (TextView) findViewById(R.id.actionbar_right_title);
        if (getActionBarRightButtonRes() <= 0){
            rightButton.setVisibility(View.INVISIBLE);
        }else {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setImageResource(getActionBarRightButtonRes());
        }

        if (getActionBarRightTitleRes() <= 0){
            rightTitle.setVisibility(View.INVISIBLE);
        }else {
            rightTitle.setVisibility(View.VISIBLE);
            rightTitle.setText(getActionBarRightTitleRes());
        }

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickActionBarTitle();
            }
        });

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

    public void updateTitleText(String text){
        if (title != null){
            title.setText(text);
        }
    }

    public void updateImageResource(int resId){
        if (back != null){
            back.setImageResource(resId);
        }
    }

    public String getTitleText(){
        if (title != null){
            return title.getText().toString();
        }
        return "";
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

    protected void onClickActionBarTitle(){
        //subclass impl if needs
    }
}
