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

public class MoneyGotActivity extends ActionBarActivity {

    private UserGetInfoResponse userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_money_got_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        setCustomActionBar();
        updateTitleText("提现");
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
