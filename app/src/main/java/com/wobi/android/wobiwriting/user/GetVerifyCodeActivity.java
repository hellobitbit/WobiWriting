package com.wobi.android.wobiwriting.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.user.message.UserGetVerifyCodeRequest;
import com.wobi.android.wobiwriting.user.message.UserGetVerifyCodeResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.views.ClearEditText;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class GetVerifyCodeActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1000;
    public static final int RESULT_CODE_SUCCESS = 4;
    private static final String TAG = "GetVerifyCodeActivity";

    private String user_phone;
    private ClearEditText verify_code;
    private UserGetVerifyCodeResponse mVerifyCodeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verify_code_layout);
        user_phone =  getIntent().getStringExtra(RegisterActivity.USER_PHONE_KEY);
        LogUtil.d(TAG," user_phone = "+user_phone);
        initViews();
    }

    private void initViews() {
        LinearLayout  backLayout = (LinearLayout)findViewById(R.id.get_verify_code_back);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView phoneText = (TextView)findViewById(R.id.code_send_phone);
        phoneText.setText("+86"+user_phone);

        final LinearLayout  sendLayout = (LinearLayout)findViewById(R.id.send);
        sendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });

        verify_code = (ClearEditText)findViewById(R.id.phone_code);
        verify_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    if (sendLayout.getVisibility() == View.VISIBLE){
                        showErrorMsg("短信验证码已过期");
                    }else {
                        if (mVerifyCodeResponse != null &&
                                verify_code.getText().toString().equals(mVerifyCodeResponse.getVerifyCode())){
                            showErrorMsg("短信验证码验证成功");
                            setResult(RESULT_CODE_SUCCESS);
                            finish();
                        }else {
                            showErrorMsg("短信验证码错误");
                        }
                    }
                }
                return false;
            }
        });

        verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.d(TAG,"beforeTextChanged count = "+count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               if (verify_code.getText().length() == 4){
                   if (mVerifyCodeResponse != null &&
                           verify_code.getText().toString().equals(mVerifyCodeResponse.getVerifyCode())){
                       showErrorMsg("短信验证码验证成功");
                       setResult(RESULT_CODE_SUCCESS);
                       finish();
                   }else {
                       showErrorMsg("短信验证码错误");
                   }
               }
            }
        });
    }

    private void getVerifyCode(){
        UserGetVerifyCodeRequest request = new UserGetVerifyCodeRequest();
        request.setPhoneNumber(user_phone);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserGetVerifyCodeResponse userGetVerifyCodeResponse = gson.fromJson(response,
                        UserGetVerifyCodeResponse.class);
                if (userGetVerifyCodeResponse != null){
                    mVerifyCodeResponse = userGetVerifyCodeResponse;
                    updateUIDisplay();
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void updateUIDisplay(){
        final TextView countDownText = (TextView)findViewById(R.id.countdown);
        ((LinearLayout)findViewById(R.id.send)).setVisibility(View.GONE);
        ((LinearLayout)findViewById(R.id.re_send)).setVisibility(View.VISIBLE);
        /** 倒计时60秒，一次1秒 */
        CountDownTimer timer = new CountDownTimer(120*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownText.setText(" ( "+millisUntilFinished/1000+"s ) ");
            }

            @Override
            public void onFinish() {
                ((LinearLayout)findViewById(R.id.send)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.re_send)).setVisibility(View.GONE);
                mVerifyCodeResponse = null;
            }
        }.start();
    }
}
