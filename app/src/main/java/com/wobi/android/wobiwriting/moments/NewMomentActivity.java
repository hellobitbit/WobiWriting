package com.wobi.android.wobiwriting.moments;

import android.os.Bundle;

import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.moments.adapters.CreateCommunityRequest;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class NewMomentActivity extends NewOrModifyMomentBaseActivity{

    private static final String TAG = "NewMomentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitleText("创建圈子");
        updateRightText("确定");
    }

    @Override
    public void onClickActionBarTitle(){
        //new moment
//        createCommunity();
    }


    private void createCommunity(){
        CreateCommunityRequest request = new CreateCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        request.setAddress(userInfo.getAddress());
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
