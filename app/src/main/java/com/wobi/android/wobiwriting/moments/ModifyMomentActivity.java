package com.wobi.android.wobiwriting.moments;

import android.content.Intent;
import android.os.Bundle;

import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.message.ChangeCommunityInfoRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.moments.model.MomentData;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class ModifyMomentActivity extends NewOrModifyMomentBaseActivity{
    public static final int REQUEST_CODE = 1019;
    public static final int RESULT_CODE_SUCCESS = 0x19;
    private static final String TAG = "NewMomentActivity";
    private MomentData momentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        momentData = (MomentData)getIntent().getSerializableExtra(MomentIntroduceActivity.MOMENT_DATA);
        updateTitleText("圈子信息修改");
        updateRightText("保存");
        updateInfo();
        refreshUI();
    }

    private void updateInfo() {
        if (momentData != null){
            CommunityInfo  communityInfo = momentData.getCommunityInfo();
            community_name = communityInfo.getCommunity_name();
            community_description = communityInfo.getSummary();
            community_address = communityInfo.getAddress();
            isAuth = communityInfo.getIs_auth() == 1 ?true : false;
            city_code = communityInfo.getCity_code();
        }
    }

    @Override
    public void onClickActionBarTextView(){
        //new moment
        if (community_address.equals("")|| community_name.equals("")){
            showErrorMsg("圈名或地址不能为空");
            return;
        }
        modifyCommunity();
    }

    private void modifyCommunity(){
        ChangeCommunityInfoRequest request = new ChangeCommunityInfoRequest();
        request.setCommunity_id(momentData.getCommunityInfo().getId());
        request.setAddress(community_address);
        request.setCity_code(city_code);
        request.setIs_auth(isAuth ? 1 : 0);
        request.setCommunity_name(community_name);
        request.setSummary(community_description);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response result = gson.fromJson(response, Response.class);
                if (result != null && result.getHandleResult().equals("OK")){
                    showErrorMsg("更新圈子成功");
                    CommunityInfo communityInfo = momentData.getCommunityInfo();
                    communityInfo.setCity_code(city_code);
                    communityInfo.setSummary(community_description);
                    communityInfo.setIs_auth(isAuth?1:0);
                    communityInfo.setAddress(community_address);
                    communityInfo.setCommunity_name(community_name);
                    Intent intent = new Intent();
                    intent.putExtra("data",communityInfo);
                    setResult(RESULT_CODE_SUCCESS, intent);
                    finish();
                }else {
                    showErrorMsg("更新圈子失败");
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
