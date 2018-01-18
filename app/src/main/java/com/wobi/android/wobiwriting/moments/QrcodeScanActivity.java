package com.wobi.android.wobiwriting.moments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.message.JoinCommunityByRequestCodeRequest;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.user.message.UserLoginResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.zxing.QRCodeView;
import com.wobi.android.wobiwriting.zxing.ZXingView;

/**
 * Created by wangyingren on 2017/12/23.
 */

public class QrcodeScanActivity extends ActionBarActivity implements QRCodeView.Delegate {

    private static final String TAG = "QrcodeScanActivity";
    private UserGetInfoResponse userInfo;
    private ZXingView mQRCodeView;

    public static final int REQUEST_CODE = 999;
    public static final int RESULT_CODE = 100;
    public static final String GET_MOMENT_REQUEST_CODE = "get_moment_request_code";

    private boolean getMomentRequestCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan_layout);
        setCustomActionBar();
        updateTitleText("扫一扫");

        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);

        getMomentRequestCode = getIntent().getBooleanExtra(GET_MOMENT_REQUEST_CODE, false);
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
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
    }

    @Override
    public void onResume(){
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    public void onPause(){
        super.onPause();
        mQRCodeView.stopSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtil.d(TAG, "request_code:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
//        mQRCodeView.startSpot();
        if (getMomentRequestCode){
            Intent data = new Intent();
            data.putExtra("qr_scan_request_code", result);
            setResult(RESULT_CODE, data);
            finish();
        }else {
            addToMoment(result);
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.d(TAG, "打开相机出错");
    }

    private void addToMoment(final String request_code){
        String loginInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
        final UserLoginResponse info  = gson.fromJson(loginInfo, UserLoginResponse.class);
        final JoinCommunityByRequestCodeRequest request = new JoinCommunityByRequestCodeRequest();
        request.setUser_id(Integer.parseInt(info.getUserId()));
        request.setRequest_code(request_code);
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
//                    infoForPurchase.setrequest_code(request_code);
//                    storeCommunityInfos(infoForPurchase);
                }else {
                    showErrorMsg("加入圈子失败");
                    mQRCodeView.startSpot();
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
                mQRCodeView.startSpot();
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
        pop.showAtLocation(findViewById(R.id.zxingview), Gravity.TOP|Gravity.START, 0, 0);
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
//            if (communityInfos.isContains(info)){
//                communityInfos.updateCommunityInfo(info);
//            }else {
//                communityInfos.getCommunityInfos().add(info);
//            }
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
