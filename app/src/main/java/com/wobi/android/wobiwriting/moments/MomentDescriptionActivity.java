package com.wobi.android.wobiwriting.moments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.message.QuitCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.moments.model.JoinMomentObj;
import com.wobi.android.wobiwriting.moments.model.CommunityInfos;
import com.wobi.android.wobiwriting.moments.model.MomentData;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.DateUtils;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;
import com.wobi.android.wobiwriting.views.CustomSettingBar;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class MomentDescriptionActivity extends ActionBarActivity {

    public static final int REQUEST_CODE = 1011;
    public static final int RESULT_CODE_SUCCESS = 0x11;
    public static final String RESULT_COMMUNITY_ID = "result_community_id";
    public static final String MOMENT_DATA = "moment_data";
    public static final String TAG = "MomentDescriptionActivity";
    private UserGetInfoResponse userInfo;
    private MomentData momentData;
    private TextView moment_modify;
    private CustomSettingBar modify_moment_name_bar;
    private CustomSettingBar moment_request_code_bar;
    private CustomSettingBar moment_exit_bar;
    private CustomSettingBar moment_make_money_bar;
    private TextView moment_summary;
    private ImageView moment_owned;
    private TextView moment_name;
    private TextView moment_position;
    private TextView moment_privacy;
    private View exit_moment_margin;

    private boolean needToRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_description_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        momentData = (MomentData)getIntent().getSerializableExtra(MOMENT_DATA);
        setCustomActionBar();
        updateTitleText("圈子介绍");
        initViews();
        refreshUI();
    }

    private void refreshUI() {
        if (momentData == null){
            return;
        }
        CommunityInfo communityInfo = momentData.getCommunityInfo();
        moment_name.setText(communityInfo.getCommunity_name());
        if (communityInfo.getIs_auth() == 0){
            moment_privacy.setText("公开");
        }else {
            moment_privacy.setText("私密");
        }
        moment_position.setText(momentData.getProvinceMap().get(communityInfo.getCity_code()));

        if (communityInfo.getUser_id() == userInfo.getUserId()){
            moment_make_money_bar.setVisibility(View.VISIBLE);
            moment_owned.setVisibility(View.VISIBLE);
            moment_modify.setVisibility(View.VISIBLE);
            moment_exit_bar.setVisibility(View.GONE);
            exit_moment_margin.setVisibility(View.GONE);
        }else {
            moment_modify.setVisibility(View.GONE);
            moment_make_money_bar.setVisibility(View.GONE);
            moment_owned.setVisibility(View.GONE);
            moment_exit_bar.setVisibility(View.VISIBLE);
            exit_moment_margin.setVisibility(View.VISIBLE);
        }
        moment_summary.setText(communityInfo.getSummary());
        moment_request_code_bar.setRightText(communityInfo.getRequest_code());
    }

    private void initViews() {
        moment_owned = (ImageView)findViewById(R.id.moment_owned);
        moment_name = (TextView)findViewById(R.id.moment_name);
        moment_position = (TextView)findViewById(R.id.moment_position);
        moment_privacy = (TextView)findViewById(R.id.moment_privacy);
        moment_summary = (TextView)findViewById(R.id.moment_summary);
        moment_modify = (TextView)findViewById(R.id.moment_modify);
        modify_moment_name_bar = (CustomSettingBar)findViewById(R.id.modify_moment_name_bar);
        moment_request_code_bar = (CustomSettingBar)findViewById(R.id.moment_request_code_bar);
        moment_exit_bar = (CustomSettingBar)findViewById(R.id.moment_exit_bar);
        moment_make_money_bar = (CustomSettingBar)findViewById(R.id.moment_make_money_bar);
        exit_moment_margin = (View)findViewById(R.id.exit_moment_margin);

        moment_make_money_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorMsg("该版本未有此功能，敬请期待");
            }
        });

        moment_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModifyMomentActivity.class);
                intent.putExtra(MomentDescriptionActivity.MOMENT_DATA,momentData);
                startActivityForResult(intent, ModifyMomentActivity.REQUEST_CODE);
            }
        });

        moment_exit_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(MomentDescriptionActivity.this);
                builder.setMessage("是否退出此圈子");
                builder.setMessageType(CustomDialog.MessageType.TextView);
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        sendExitMomentRequest();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
    }

    private void sendExitMomentRequest() {
        QuitCommunityRequest request = new QuitCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        request.setCommunity_id(momentData.getCommunityInfo().getId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                Response rsp =  gson.fromJson(response, Response.class);
                if (rsp != null && rsp.getHandleResult().equals("OK")){
                    JoinMomentObj infoForPurchase = new JoinMomentObj();
                    infoForPurchase.setJoin_community_time(DateUtils.getCurrentTime());
                    infoForPurchase.setrequest_code(momentData.getCommunityInfo().getRequest_code());
                    deleteAndUpdateCommunityInfos(infoForPurchase);
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_COMMUNITY_ID, momentData.getCommunityInfo().getId());
                    setResult(RESULT_CODE_SUCCESS, intent);
                    finish();
                }else {
                    showErrorMsg("退出圈子失败");
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == ModifyMomentActivity.REQUEST_CODE &&
                resultCode == ModifyMomentActivity.RESULT_CODE_SUCCESS){
            if (data != null){
                CommunityInfo communityInfo = (CommunityInfo)data.getSerializableExtra("data");
                momentData.setCommunityInfo(communityInfo);
                needToRefresh = true;
                refreshUI();
            }
        }
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

    @Override
    protected void onClickBack() {
        LogUtil.d(TAG, "onClickBack needToRefresh = "+ needToRefresh);
        if (needToRefresh){
            Intent intent = new Intent();
            intent.putExtra("data", momentData.getCommunityInfo());
            setResult(RESULT_CODE_SUCCESS, intent);
        }
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (needToRefresh){
                Intent intent = new Intent();
                intent.putExtra("data", momentData.getCommunityInfo());
                setResult(RESULT_CODE_SUCCESS, intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void deleteAndUpdateCommunityInfos(JoinMomentObj info){
        String communityInfosStr = SharedPrefUtil.getCommunityInfosForPurchase(getApplicationContext());
        CommunityInfos communityInfos = null;
        if (!TextUtils.isEmpty(communityInfosStr) && info != null) {
            communityInfos = gson.fromJson(communityInfosStr, CommunityInfos.class);
            communityInfos.deleteCommunityInfo(info.getRequest_code());
        }

        communityInfosStr =  gson.toJson(communityInfos);
        SharedPrefUtil.saveCommunityInfosForPurchase(getApplicationContext(), communityInfosStr);

        LogUtil.d(TAG," deleteAndUpdateCommunityInfos == "+ communityInfosStr);
    }
}
