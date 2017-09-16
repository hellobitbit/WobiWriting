package com.wobi.android.wobiwriting.me;

import com.wobi.android.wobiwriting.ui.CustomActionBarActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.views.CustomDialog;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class MyInformationActivity extends CustomActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information_layout);
        initViews();

    }

    private void initViews(){
        RelativeLayout headPortraitBar = (RelativeLayout)findViewById(R.id.my_info_user_head_portrait_bar);
        RelativeLayout userNameBar = (RelativeLayout)findViewById(R.id.my_info_user_name_bar);
        RelativeLayout uderDescriptionBar = (RelativeLayout)findViewById(R.id.my_info_user_description_bar);

        headPortraitBar.setOnClickListener(this);
        userNameBar.setOnClickListener(this);
        uderDescriptionBar.setOnClickListener(this);
    }

    @Override
    public void onClickActionBarTextView(){
        //bussiness
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_personal_info_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return R.string.action_bar_right_title_confirm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_info_user_description_bar:
                break;
            case R.id.my_info_user_name_bar:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("这个就是自定义的提示框");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
                break;
            case R.id.my_info_user_head_portrait_bar:
                break;
        }
    }
}
