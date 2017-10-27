package com.wobi.android.wobiwriting.me;

import android.os.Bundle;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.moments.message.SearchJoinedCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.SearchOwnedCommunityRequest;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyMomentActivity extends ActionBarActivity {

    private static final String TAG = "MyMomentActivity";
    private UserGetInfoResponse userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment_layout);
        initViews();
        setCustomActionBar();
        initData();
    }

    private void initViews(){

    }

    private void initData(){
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        searchOwnedCommunity();
        searchJoinedCommunity();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_my_moment_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    private void searchOwnedCommunity(){
        SearchOwnedCommunityRequest request = new SearchOwnedCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void searchJoinedCommunity(){
        SearchJoinedCommunityRequest request = new SearchJoinedCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }
}
