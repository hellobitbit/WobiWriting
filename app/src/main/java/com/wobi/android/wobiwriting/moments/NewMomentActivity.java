package com.wobi.android.wobiwriting.moments;

import android.os.Bundle;

import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.moments.message.CreateCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.CreateCommunityResponse;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class NewMomentActivity extends NewOrModifyMomentBaseActivity{

    public static final int REQUEST_CODE = 1010;
    public static final int RESULT_CODE_SUCCESS = 0x10;
    private static final String TAG = "NewMomentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitleText("创建圈子");
        updateRightText("确定");
    }

    @Override
    public void onClickActionBarTextView(){
        //new moment
        if (community_address.equals("")|| community_name.equals("")){
            showErrorMsg("圈名或地址不能为空");
            return;
        }
        createCommunity();
    }


    private void createCommunity(){
        CreateCommunityRequest request = new CreateCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        request.setAddress(community_address);
        request.setIs_alive(1);
        request.setIs_auth(isAuth ? 1 : 0);
        request.setCommunity_name(community_name);
        request.setSummary(community_description);
        request.setCity_code(city_code);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                CreateCommunityResponse createCommunityResponse = gson.fromJson(response,
                        CreateCommunityResponse.class);
                if (createCommunityResponse != null && createCommunityResponse.getCreate_result() == 1){
                    showErrorMsg("创建圈子成功");
                    setResult(RESULT_CODE_SUCCESS);
                    finish();
                }else {
                    showErrorMsg("创建圈子失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }
}
