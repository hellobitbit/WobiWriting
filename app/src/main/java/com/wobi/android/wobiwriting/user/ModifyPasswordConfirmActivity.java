package com.wobi.android.wobiwriting.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;

/**
 * Created by wangyingren on 2018/4/22.
 */

public class ModifyPasswordConfirmActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password_confirm_layout);
        initViews();
        setCustomActionBar();
        updateTitleText("修改密码");
    }

    private void initViews() {

        TextView phone_verify = (TextView)findViewById(R.id.phone_verify);
        phone_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneVerification = new Intent(ModifyPasswordConfirmActivity.this,
                        VerifyCodeActivity.class);
                startActivity(phoneVerification);
                finish();
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
}
