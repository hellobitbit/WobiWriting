package com.wobi.android.wobiwriting.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.DateUtils;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangyingren on 2017/12/23.
 */

public class MyVipActivity extends ActionBarActivity {

    private static final String TAG = "MyVipActivity";
    private UserGetInfoResponse userInfoResponse;
    private ImageView user_headphoto;
    private TextView user_name;
    private TextView expire_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip_layout);
        setCustomActionBar();
        updateTitleText("我的VIP会员");
        initViews();
        refreshUserInfo();
    }

    private void initViews(){
        findViewById(R.id.purchase_right_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PurchaseVipActivity.class);
                startActivityForResult(intent, PurchaseVipActivity.REQUEST_CODE);
            }
        });

        findViewById(R.id.continue_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PurchaseVipActivity.class);
                startActivityForResult(intent, PurchaseVipActivity.REQUEST_CODE);
            }
        });

        user_headphoto =(ImageView)findViewById(R.id.user_headphoto);
        user_name =(TextView)findViewById(R.id.user_name);
        expire_time =(TextView)findViewById(R.id.expire_time);
    }

    private void refreshUserInfo(){
        String userInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
        LogUtil.d(TAG, " refreshLoginState userInfo == " + userInfo);
        if (userInfo != null && !userInfo.isEmpty()) {
            userInfoResponse = gson.fromJson(userInfo, UserGetInfoResponse.class);
            if (userInfoResponse.getName() == null ||
                    userInfoResponse.getName().isEmpty()) {
                user_name.setText("无");
            } else {
                user_name.setText(userInfoResponse.getName());
            }
            if (userInfoResponse.getSex() == 0) {
                //men
                user_headphoto.setImageResource(R.drawable.default_man_headphoto);
            } else if (userInfoResponse.getSex() == 1) {
                //girl
                user_headphoto.setImageResource(R.drawable.deafault_girl_headphoto);
            }
            if (userInfoResponse.getIs_vip() == 1 && !isExpired(userInfoResponse.getVip_expire_time())){
                expire_time.setText(DateUtils.parseDateToVipData(userInfoResponse.getVip_expire_time())
                +" 到期");
            }else {

                expire_time.setText("尚未购买VIP");
            }
        }
    }

    private static boolean isExpired(String vip_expire_time){
        LogUtil.e(TAG,"isExpire = "+compare_date(vip_expire_time));
        return compare_date(vip_expire_time)!=1;
    }

    private static int compare_date(String vip_expire_time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(vip_expire_time);
            Date dt2 = new Date();
            if (dt1.getTime() >= dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
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

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == PurchaseVipActivity.REQUEST_CODE){
            refreshUserInfo();
        }
    }
}
