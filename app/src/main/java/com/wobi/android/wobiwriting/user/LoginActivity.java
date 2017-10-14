package com.wobi.android.wobiwriting.user;

import android.content.Intent;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.http.HttpConfig;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.user.message.UserLoginRequest;
import com.wobi.android.wobiwriting.user.message.UserLoginResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/10/13.
 */

public class LoginActivity extends AccountBaseActivity{
    public static final int REQUEST_CODE = 1001;
    public static final int RESULT_CODE_SUCCESS = 0x5;
    private static final String TAG = "LoginActivity";

    @Override
    void updateViewsState() {
        login_register_label.setText(getResources().getString(R.string.user_login_label));
        login_register_switch.setText(getResources().getString(R.string.user_login_to_register_label));
        confirm_password_edit.setVisibility(View.GONE);
        request_code_edit.setVisibility(View.GONE);
        login_or_register.setText(getResources().getString(R.string.user_login_label));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_register_switch:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_or_register:
                login();
                break;
        }
    }

    private void login(){
        if (password_edit.getText().toString().isEmpty()
                || phone_edit.getText().toString().isEmpty()){
            showErrorMsg("输入不能为空");
            return;
        }

        UserLoginRequest request = new UserLoginRequest();
        request.setPhoneNumber(phone_edit.getText().toString());
        request.setPassword(password_edit.getText().toString());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserLoginResponse userLoginResponse = gson.fromJson(response, UserLoginResponse.class);
                if (userLoginResponse.getHandleResult().equals("OK")) {
                    if (userLoginResponse.getLoginResult() == 1) {
                        SharedPrefUtil.saveSessionId(getApplicationContext(), userLoginResponse.getSession_id());
                        HttpConfig.setSessionId(userLoginResponse.getSession_id());
                        getUserInfo(userLoginResponse);
                    } else if (userLoginResponse.getLoginResult() == 2) {
                        showErrorMsg("登录失败");
                    }
                }else {
                    showErrorMsg("登录失败 "+userLoginResponse.getHandleResult());
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void getUserInfo(UserLoginResponse userLoginResponse){
        UserGetInfoRequest request = new UserGetInfoRequest();
        request.setUserId(Integer.parseInt(userLoginResponse.getUserId()));
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserGetInfoResponse userGetInfoResponse = gson.fromJson(response, UserGetInfoResponse.class);
                if (userGetInfoResponse.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginInfo(getApplicationContext(),response);
                    //保存登录信息
                    setResult(RESULT_CODE_SUCCESS);
                    showErrorMsg("登录成功");
                    finish();
                }else {
                    showErrorMsg("登录失败 "+ userGetInfoResponse.getHandleResult());
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
