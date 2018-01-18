package com.wobi.android.wobiwriting.moments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2018/1/6.
 */

public class MomentGainActivity extends ActionBarActivity {

    private UserGetInfoResponse userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_gain_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        setCustomActionBar();
        updateTitleText("圈子盈利");
        updateRightText("确定");

        findViewById(R.id.get_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MomentGainActivity.this, MoneyGotActivity.class);
//                startActivity(intent);
                showErrorMsg("此功能正在内部测试中，即将开放");
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
