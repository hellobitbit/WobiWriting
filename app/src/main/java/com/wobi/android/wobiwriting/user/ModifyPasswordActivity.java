package com.wobi.android.wobiwriting.user;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.message.JoinCommunityRequest;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.ModifyPasswordRequest;
import com.wobi.android.wobiwriting.user.message.UserCommitRegisterInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserLoginResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2018/5/8.
 */

public class ModifyPasswordActivity extends ActionBarActivity {

    private static final String TAG = "ModifyPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password_layout);
        initViews();
        setCustomActionBar();
        updateTitleText("修改密码");
    }

    private void initViews() {
        final EditText newPassword = (EditText)findViewById(R.id.new_pw);
        final EditText newPasswordConfirm = (EditText)findViewById(R.id.new_pw_confirm);
        final TextView modifyPassword = (TextView)findViewById(R.id.pw_modify_confirm);
        modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().equals("")
                        || newPasswordConfirm.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                }else if (!newPassword.getText().toString().equals(
                        newPasswordConfirm.getText().toString())){
                    Toast.makeText(getApplicationContext(), "两次输入不一致", Toast.LENGTH_SHORT).show();
                }else if (newPassword.getText().toString().equals(
                        SharedPrefUtil.getLoginPassword(getApplicationContext()))){
                    Toast.makeText(getApplicationContext(), "新密码需要与旧密码不一致",
                            Toast.LENGTH_SHORT).show();
                }else {
                    modifyPassword(newPassword.getText().toString());
                }
            }
        });
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return 0;
    }

    private void modifyPassword(final String password){
        ModifyPasswordRequest request = new ModifyPasswordRequest();
        request.setPassword(password);
        String loginInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
        UserLoginResponse info  = gson.fromJson(loginInfo, UserLoginResponse.class);
        request.setUserId(Integer.parseInt(info.getUserId()));
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response result = gson.fromJson(response,Response.class);
                if (result != null && result.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginPassword(getApplicationContext(), password);
                    displayPopupWindowTips(R.drawable.modify_password_success);
                }else {
                    showErrorMsg("更新密码失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void displayPopupWindowTips(int imageResId){
        View layout = getLayoutInflater().inflate(R.layout.app_overlay_layout, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        layout.setBackgroundResource(imageResId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                finish();
            }
        });
        pop.setClippingEnabled(false);
        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.showAtLocation(findViewById(R.id.addToMoment), Gravity.TOP|Gravity.START, 0, 0);
    }
}
