package com.wobi.android.wobiwriting.ui;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.user.AccountBaseActivity;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class BaseActivity extends Activity {

    protected Gson gson = new Gson();

    public void showErrorMsg(String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void showNetWorkException(){
        showErrorMsg("网络异常");
    }
}
