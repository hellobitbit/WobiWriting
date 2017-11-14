package com.wobi.android.wobiwriting.me;

import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.me.message.UserChangeInfoRequest;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.user.message.UserLogoutRequest;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;
import com.wobi.android.wobiwriting.views.CustomSettingBar;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class MyInformationActivity extends ActionBarActivity implements View.OnClickListener{

    public static final int REQUEST_CODE = 1100;
    public static final int RESULT_CODE_SUCCESS = 0x10;

    public static final String USER_INFO = "user_info";
    private static final String TAG = "MyInformationActivity";
    private CustomSettingBar userNameBar;
    private CustomSettingBar userDescriptionBar;
    private UserGetInfoResponse userInfo;
    private ImageView head_portrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information_layout);
        userInfo = (UserGetInfoResponse)getIntent().getSerializableExtra(USER_INFO);
        initViews();
        setCustomActionBar();
        if (userInfo != null){
            refreshUserInfoDisplay();
        }
    }

    private void refreshUserInfoDisplay() {
        if (userInfo.getSex() == 0) {
            //men
            head_portrait.setImageResource(R.drawable.default_man_headphoto);
        } else if (userInfo.getSex() == 1) {
            //girl
            head_portrait.setImageResource(R.drawable.deafault_girl_headphoto);
        }

        if (userInfo.getName() == null ||
                userInfo.getName().isEmpty()) {
            userNameBar.setRightText("无");
        } else {
            userNameBar.setRightText(userInfo.getName());
        }
    }

    private void initViews(){
        RelativeLayout headPortraitBar = (RelativeLayout)findViewById(R.id.my_info_user_head_portrait_bar);
        head_portrait = (ImageView)findViewById(R.id.my_info_user_head_portrait);
        userNameBar = (CustomSettingBar)findViewById(R.id.my_info_user_name_bar);
        userDescriptionBar = (CustomSettingBar)findViewById(R.id.my_info_user_description_bar);

        userNameBar.setRightText("text1");
        userDescriptionBar.setRightText("无");

        headPortraitBar.setOnClickListener(this);
        userNameBar.setOnClickListener(this);
        userDescriptionBar.setOnClickListener(this);
        userDescriptionBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickActionBarTextView(){
        //bussiness
        if (userInfo == null){
            showErrorMsg("当前存在异常，更新失败");
        }else {
            if (!userNameBar.getRightText().equals(userInfo.getName())){
                updateUserInfo();
            }else {
                showErrorMsg("未有资料需要更新");
            }
        }

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
                final CustomDialog.Builder userNameBarBuilder = new CustomDialog.Builder(this);
                userNameBarBuilder.setMessage(userNameBar.getRightText());
                userNameBarBuilder.setMessageType(CustomDialog.MessageType.EditText);
                userNameBarBuilder.setTitle("名字修改");
                userNameBarBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        if (dialog instanceof CustomDialog){
                            CustomDialog customDialog =(CustomDialog)dialog;
                            EditText editText = (EditText) customDialog.findViewById(R.id.edit_message);
                            LogUtil.d(TAG, "editText = "+editText.getText().toString());
                            userNameBar.setRightText(editText.getText().toString());
                        }
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

    private void updateUserInfo(){
        UserChangeInfoRequest request = new UserChangeInfoRequest();
        request.setUser_id(userInfo.getUserId());
        request.setAddress(userInfo.getAddress());
        request.setPassword(SharedPrefUtil.getLoginPassword(getApplicationContext()));
        request.setSex(userInfo.getSex());
        request.setUser_name(userNameBar.getRightText());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response res = gson.fromJson(response,Response.class);
                if (res != null && res.getHandleResult().equals("OK")){
                    userInfo.setName(userNameBar.getRightText());
                    String result = gson.toJson(userInfo);
                    LogUtil.d(TAG," result: "+result);
                    SharedPrefUtil.saveLoginInfo(getApplicationContext(),result);
                    showErrorMsg("更新成功");
                }else {
                    showErrorMsg("更新失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }
}
