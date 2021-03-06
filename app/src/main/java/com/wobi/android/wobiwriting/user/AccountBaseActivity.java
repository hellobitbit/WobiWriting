package com.wobi.android.wobiwriting.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.ClearEditText;

/**
 * Created by wangyingren on 2017/10/13.
 */

public abstract class AccountBaseActivity extends BaseActivity implements View.OnClickListener{

    protected ClearEditText phone_edit;
    protected ClearEditText password_edit;
    protected ClearEditText confirm_password_edit;
    protected TextView login_register_label;
    protected TextView login_register_switch;
    protected TextView login_or_register;
    protected ClearEditText request_code_edit;
    protected RelativeLayout request_code_layout;
    protected ImageView register_scan;
    protected TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_base_layout);
        initViews();
        updateViewsState();
    }

    private void initViews(){
        ImageButton exit = (ImageButton)findViewById(R.id.login_register_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login_register_label = (TextView)findViewById(R.id.login_register_label);
        login_register_switch = (TextView)findViewById(R.id.login_register_switch);
        login_register_switch.setOnClickListener(this);
        login_or_register = (TextView)findViewById(R.id.login_or_register);
        login_or_register = (TextView)findViewById(R.id.login_or_register);
        forgetPassword = (TextView)findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(this);
        login_or_register.setOnClickListener(this);
        phone_edit = (ClearEditText)findViewById(R.id.phone);
        phone_edit.setText(SharedPrefUtil.getLastLoginAccount(getApplicationContext()));
        password_edit = (ClearEditText)findViewById(R.id.password);
        confirm_password_edit = (ClearEditText)findViewById(R.id.confirm_password);
        request_code_edit = (ClearEditText)findViewById(R.id.request_code);
        request_code_layout = (RelativeLayout)findViewById(R.id.request_code_layout);
        register_scan = (ImageView)findViewById(R.id.register_scan);
        register_scan.setOnClickListener(this);
    }

    abstract void updateViewsState();
}
