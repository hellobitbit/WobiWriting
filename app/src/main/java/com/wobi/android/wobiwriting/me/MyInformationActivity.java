package com.wobi.android.wobiwriting.me;

import com.wobi.android.wobiwriting.ui.ActionBarActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.views.CustomDialog;
import com.wobi.android.wobiwriting.views.CustomSettingBar;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class MyInformationActivity extends ActionBarActivity implements View.OnClickListener{

    private CustomSettingBar userNameBar;
    private CustomSettingBar userDescriptionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information_layout);
        initViews();
        setCustomActionBar();
    }

    private void initViews(){
        RelativeLayout headPortraitBar = (RelativeLayout)findViewById(R.id.my_info_user_head_portrait_bar);
        userNameBar = (CustomSettingBar)findViewById(R.id.my_info_user_name_bar);
        userDescriptionBar = (CustomSettingBar)findViewById(R.id.my_info_user_description_bar);

        userNameBar.setRightText("text1");
        userDescriptionBar.setRightText("text2");

        headPortraitBar.setOnClickListener(this);
        userNameBar.setOnClickListener(this);
        userDescriptionBar.setOnClickListener(this);
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
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage(userDescriptionBar.getRightText());
                builder.setMessageType(CustomDialog.MessageType.EditText);
                builder.setTitle("介绍修改");
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
            case R.id.my_info_user_name_bar:
                CustomDialog.Builder userNameBarBuilder = new CustomDialog.Builder(this);
                userNameBarBuilder.setMessage(userNameBar.getRightText());
                userNameBarBuilder.setMessageType(CustomDialog.MessageType.EditText);
                userNameBarBuilder.setTitle("名字修改");
                userNameBarBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                    }
                });

                userNameBarBuilder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                userNameBarBuilder.create().show();
                break;
            case R.id.my_info_user_head_portrait_bar:

                break;
        }
    }
}
