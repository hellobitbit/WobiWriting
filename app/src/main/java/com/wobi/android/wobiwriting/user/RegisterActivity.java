package com.wobi.android.wobiwriting.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.QrcodeScanActivity;
import com.wobi.android.wobiwriting.user.message.UserCommitRegisterInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserVerifyPhoneRequest;
import com.wobi.android.wobiwriting.user.message.UserVerifyPhoneResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class RegisterActivity extends AccountBaseActivity{
    public static final int REQUEST_CODE = 1101;
    public static final int RESULT_CODE_SUCCESS = 0x15;
    public static final String USER_PHONE = "user_phone";
    public static final String USER_PASSWORD = "user_password";
    private static final String TAG = "RegisterActivity";

    public static final String USER_PHONE_KEY = "user_phone_key";

    private boolean startedByHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startedByHome = getIntent().getBooleanExtra("home_start", false);
    }

    @Override
    void updateViewsState() {
        login_register_label.setText(getResources().getString(R.string.user_register_label));
        login_register_switch.setText(getResources().getString(R.string.user_register_to_login_label));
        confirm_password_edit.setVisibility(View.VISIBLE);
        request_code_layout.setVisibility(View.VISIBLE);
        forgetPassword.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_register_switch:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                if (startedByHome){
                    finish();
                }
                break;
            case R.id.login_or_register:
                register();
                break;
            case R.id.register_scan:
                Intent scan = new Intent(this, QrcodeScanActivity.class);
                scan.putExtra(QrcodeScanActivity.GET_MOMENT_REQUEST_CODE, true);
                startActivityForResult(scan, QrcodeScanActivity.REQUEST_CODE);
                break;
        }
    }

    private void verifyPhoneCanUsed(){
        UserVerifyPhoneRequest request = new UserVerifyPhoneRequest();
        request.setPhoneNumber(phone_edit.getText().toString());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserVerifyPhoneResponse userVerifyPhoneResponse = gson.fromJson(response,
                        UserVerifyPhoneResponse.class);
                if (userVerifyPhoneResponse != null) {
                    if (userVerifyPhoneResponse.getPhoneStatus() == 1) {
                        Intent intent = new Intent(getApplicationContext(), GetVerifyCodeActivity.class);
                        intent.putExtra(USER_PHONE_KEY, phone_edit.getText().toString());
                        startActivityForResult(intent, GetVerifyCodeActivity.REQUEST_CODE);
                    } else if (userVerifyPhoneResponse.getPhoneStatus() == 2) {
                        showErrorMsg("该手机号码已经注册");
                    } else if (userVerifyPhoneResponse.getPhoneStatus() == 3) {
                        showErrorMsg("该手机号码存在异常");
                    }
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void register(){
        if (password_edit.getText().toString().isEmpty()
                || phone_edit.getText().toString().isEmpty()
                || confirm_password_edit.getText().toString().isEmpty()){
            showErrorMsg("电话或者密码输入不能为空");
            return;
        }

        if (request_code_edit.getText().toString().isEmpty()){
            showErrorMsg("邀请码输入不能为空");
            return;
        }

        if (!password_edit.getText().toString().
                equals(confirm_password_edit.getText().toString())){
            showErrorMsg("两次密码输入不一致");
            return;
        }

        verifyPhoneCanUsed();
    }


    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == GetVerifyCodeActivity.REQUEST_CODE
                && resultCode == GetVerifyCodeActivity.RESULT_CODE_SUCCESS){
            commitRegisterInfo();
        }else if (requestCode == QrcodeScanActivity.REQUEST_CODE
                && resultCode == QrcodeScanActivity.RESULT_CODE){
            String request_code = data.getStringExtra("qr_scan_request_code");
            LogUtil.d(TAG," data = "+request_code);
            request_code_edit.setText(request_code);
        }
    }

    private void commitRegisterInfo(){
        UserCommitRegisterInfoRequest request = new UserCommitRegisterInfoRequest();
        request.setRequestCode(request_code_edit.getText().toString());
        request.setPhoneNumber(phone_edit.getText().toString());
        request.setPassword(password_edit.getText().toString());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response userCommitRegisterResponse = gson.fromJson(response, Response.class);
                if (userCommitRegisterResponse != null){
                    Intent intent = new Intent();
                    intent.putExtra(USER_PHONE,phone_edit.getText().toString());
                    intent.putExtra(USER_PASSWORD,password_edit.getText().toString());
                    if (startedByHome){
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.putExtra(USER_PHONE,phone_edit.getText().toString());
                        login.putExtra(USER_PASSWORD,password_edit.getText().toString());
                        startActivity(login);
                    }else {
                        setResult(RESULT_CODE_SUCCESS, intent);
                    }
                    showErrorMsg("注册成功");
                    finish();
                }else {
                    showErrorMsg("注册失败,数据异常");
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }
}
