package com.wobi.android.wobiwriting.moments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.message.JoinCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.user.message.UserLoginResponse;
import com.wobi.android.wobiwriting.utils.DateUtils;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/10/24.
 */

public class MomentDetailActivity extends ActionBarActivity {

    public static final String COMMUNITY_INFO ="community_info";
    private static final String TAG = "MomentDetailActivity";
    private ImageView moment_icon;
    private TextView moment_name;
    private TextView moment_code;
    private TextView moment_time;
    private TextView moment_summary;
    private RelativeLayout favor_layout;
    private RelativeLayout comments_layout;
    private RelativeLayout forward_layout;
    private TextView favor;
    private TextView comments;
    private TextView forward;
    private CommunityInfo communityInfo;
    private UserGetInfoResponse userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail_layout);
        communityInfo = (CommunityInfo)getIntent().getSerializableExtra(COMMUNITY_INFO);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        initViews();
        setCustomActionBar();
        updateTitleText("圈子详情");
        if (communityInfo != null){
            updateUI();
        }
    }

    private void updateUI() {
        moment_name.setText(communityInfo.getCommunity_name());
        moment_code.setText("邀请码："+communityInfo.getRequest_code());
        moment_summary.setText(communityInfo.getSummary());
        moment_time.setText(DateUtils.parseDateString(communityInfo.getCreate_time()));
    }

    private void initViews() {
        moment_icon = (ImageView)findViewById(R.id.moment_icon);
        moment_name = (TextView)findViewById(R.id.moment_name);
        moment_code = (TextView)findViewById(R.id.moment_code);
        moment_time = (TextView)findViewById(R.id.moment_time);
        moment_summary = (TextView)findViewById(R.id.moment_summary);

        favor_layout = (RelativeLayout)findViewById(R.id.favor_layout);
        comments_layout = (RelativeLayout)findViewById(R.id.comments_layout);
        forward_layout = (RelativeLayout)findViewById(R.id.forward_layout);
        favor = (TextView)findViewById(R.id.favor);
        comments = (TextView)findViewById(R.id.comments);
        forward = (TextView)findViewById(R.id.forward);

        ImageView addToMoment = (ImageView)findViewById(R.id.addToMoment);
        addToMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToComment();
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

    private void addToComment(){
        String loginInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
        UserLoginResponse info  = gson.fromJson(loginInfo, UserLoginResponse.class);
        final JoinCommunityRequest request = new JoinCommunityRequest();
        request.setUser_id(Integer.parseInt(info.getUserId()));
        request.setCommunity_id(communityInfo.getId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response result = gson.fromJson(response,Response.class);
                if (result != null && result.getHandleResult().equals("OK")){
                    updateUserInfo();
                    displayPopupWindowTips(R.drawable.add_to_moment_success);
//                    JoinMomentObj infoForPurchase = new JoinMomentObj();
//                    infoForPurchase.setJoin_community_time(DateUtils.getCurrentTime());
//                    infoForPurchase.setrequest_code(communityInfo.getRequest_code());
//                    storeCommunityInfos(infoForPurchase);
                }else {
                    showErrorMsg("加入圈子失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void displayPopupWindowTips(int imageResId){
        View layout = getLayoutInflater().inflate(R.layout.app_overlay_layout, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        layout.setBackgroundResource(imageResId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pop.setClippingEnabled(false);
        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.showAtLocation(findViewById(R.id.addToMoment), Gravity.TOP|Gravity.START, 0, 0);
    }

    private void updateUserInfo(){
        UserGetInfoRequest request = new UserGetInfoRequest();
        request.setUserId(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserGetInfoResponse userGetInfoResponse = gson.fromJson(response, UserGetInfoResponse.class);
                if (userGetInfoResponse != null && userGetInfoResponse.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginInfo(getApplicationContext(),response);
                    userInfo = userGetInfoResponse;
                }else {
                    showErrorMsg("用户信息更新失败 "+ userGetInfoResponse.getHandleResult());
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

//    private void storeCommunityInfos(JoinMomentObj info){
//        String communityInfosStr = SharedPrefUtil.getCommunityInfosForPurchase(getApplicationContext());
//        CommunityInfos communityInfos = null;
//        if (!TextUtils.isEmpty(communityInfosStr)) {
//            communityInfos = gson.fromJson(communityInfosStr, CommunityInfos.class);
//            communityInfos.getCommunityInfos().add(info);
//        }else {
//            communityInfos = new CommunityInfos();
//            communityInfos.initCommunityInfos();
//            communityInfos.getCommunityInfos().add(info);
//        }
//
//        communityInfosStr =  gson.toJson(communityInfos);
//        SharedPrefUtil.saveCommunityInfosForPurchase(getApplicationContext(), communityInfosStr);
//
//        LogUtil.d(TAG," storeCommunityInfos == "+ communityInfosStr);
//    }
}
