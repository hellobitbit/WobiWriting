package com.wobi.android.wobiwriting.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.me.FeedbackActivity;
import com.wobi.android.wobiwriting.moments.MyMomentActivity;
import com.wobi.android.wobiwriting.me.MyWodouActivity;
import com.wobi.android.wobiwriting.me.MyInformationActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.user.message.UserLogoutRequest;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "MeFragment";
    private final String W_APPID = "wx811d8d46a1d5cb01";
    private ImageView user_icon;
    private TextView user_name;
    private TextView user_description;
    private TextView moments_num_value;
    private TextView follow_num_value;
    private TextView wodou_num_value;
    private UserGetInfoResponse userInfoResponse;

    private DialogInterface mLoginTipsDialog;
    private IWXAPI api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.me_frag_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        RelativeLayout userInfo = (RelativeLayout)view.findViewById(R.id.user_info);

        RelativeLayout shareApp = (RelativeLayout)view.findViewById(R.id.share_app);
        RelativeLayout userFeedback = (RelativeLayout)view.findViewById(R.id.user_feedback);
        RelativeLayout accountExit = (RelativeLayout)view.findViewById(R.id.account_exit);

        LinearLayout momentsNumLayout = (LinearLayout)view.findViewById(R.id.moments_num_layout);
        LinearLayout followNumLayout = (LinearLayout)view.findViewById(R.id.follow_num_layout);
        LinearLayout wodouNumLayout = (LinearLayout)view.findViewById(R.id.wodou_num_layout);

        shareApp.setOnClickListener(this);

        userFeedback.setOnClickListener(this);
        accountExit.setOnClickListener(this);
        userInfo.setOnClickListener(this);

        momentsNumLayout.setOnClickListener(this);
        followNumLayout.setOnClickListener(this);
        wodouNumLayout.setOnClickListener(this);

        user_icon = (ImageView)view.findViewById(R.id.user_icon);
        user_name = (TextView)view.findViewById(R.id.user_name);
        user_description = (TextView)view.findViewById(R.id.user_description);
        moments_num_value = (TextView)view.findViewById(R.id.moments_num_value);
        follow_num_value = (TextView)view.findViewById(R.id.follow_num_value);
        wodou_num_value = (TextView)view.findViewById(R.id.wodou_num_value);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        refreshLoginState();
        regToWx();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), W_APPID, true);
        api.registerApp(W_APPID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_exit:
                logout();
                break;
            case R.id.user_feedback:
                Intent feedback = new Intent(getActivity(), FeedbackActivity.class);
                getActivity().startActivity(feedback);
                break;
            case R.id.share_app:
                showPopupWindow();
                break;
            case R.id.user_info:
                Intent personalInfo = new Intent(getActivity(), MyInformationActivity.class);
                personalInfo.putExtra(MyInformationActivity.USER_INFO,userInfoResponse);
//                getActivity().startActivity(personalInfo);
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.startUserInfoActivity(personalInfo, MyInformationActivity.REQUEST_CODE);
                break;
            case R.id.moments_num_layout:
                Intent moments = new Intent(getActivity(), MyMomentActivity.class);
                getActivity().startActivity(moments);
                break;
            case R.id.follow_num_layout:
                showErrorMsg("该版本未有此功能，敬请期待");
//                Intent follow = new Intent(getActivity(), MyFollowActivity.class);
//                getActivity().startActivity(follow);
                break;
            case R.id.wodou_num_layout:
                Intent wodou = new Intent(getActivity(), MyWodouActivity.class);
                getActivity().startActivity(wodou);
                break;
        }
    }

    public void updateUIDisplay(UserGetInfoResponse response){
        if (response != null) {
            moments_num_value.setText("" + response.getCommunityCount());
            wodou_num_value.setText(response.getWobi_beans());

            if (response.getName() == null ||
                    response.getName().isEmpty()) {
                user_name.setText("无");
            } else {
                user_name.setText(response.getName());
            }
            user_description.setText("无");
            if (response.getSex().equals("0")) {
                //men
                user_icon.setImageResource(R.drawable.default_man_headphoto);
            } else if (response.getSex().equals("1")) {
                //girl
                user_icon.setImageResource(R.drawable.deafault_girl_headphoto);
            }
        }else {
            moments_num_value.setText("0");
            wodou_num_value.setText("0");
            user_name.setText(getResources().getString(R.string.app_name));
            user_description.setText(getResources().getString(R.string.default_user_description));
        }
    }

    void refreshLoginState(){
        String userInfo = SharedPrefUtil.getLoginInfo(getActivity());
        LogUtil.d(TAG, " refreshLoginState userInfo == "+userInfo);
        if (userInfo != null && !userInfo.isEmpty()){
            userInfoResponse = gson.fromJson(userInfo, UserGetInfoResponse.class);
            updateUIDisplay(userInfoResponse);
        }else {
            userInfoResponse = null;
            updateUIDisplay(null);
            checkLogin();
        }

        dismissLoginTipsDialog();
    }

    private void logout(){
        if (userInfoResponse == null){
            showErrorMsg("当前未有账号登录");
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage("是否退出此账号");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                sendLogoutRequest();
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

    private void sendLogoutRequest(){
        UserLogoutRequest request = new UserLogoutRequest();
        request.setUserId(userInfoResponse.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                Response res = gson.fromJson(response,Response.class);
                if (res != null && res.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginInfo(getActivity(),"");
                    SharedPrefUtil.saveSessionId(getActivity(),"");
                    SharedPrefUtil.saveLoginPassword(getActivity(),"");
                    SharedPrefUtil.saveKewenDirectoryPosition(getActivity(), 0);
                    SharedPrefUtil.saveSZPosition(getActivity(), 0);
                    refreshLoginState();
                }else {
                    showErrorMsg("退出失败");
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void checkLogin(){
        if (userInfoResponse == null){
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("登录后才能使用此功能");
            builder.setMessageType(CustomDialog.MessageType.TextView);
            builder.setTitle("提示");
            builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mLoginTipsDialog = dialog;
                    //设置你的操作事项
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.startLoginActivity(LoginActivity.REQUEST_CODE);
                }
            });

            builder.setNegativeButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity mainActivity = (MainActivity)getActivity();
                            mainActivity.switchToPrevFragmentWhenCancelLoginTips();
                        }
                    });
            builder.setCancelable(false);
            builder.create().show();

        }
    }

    private void dismissLoginTipsDialog(){
        if (mLoginTipsDialog != null){
            mLoginTipsDialog.dismiss();
        }
    }

    private void showPopupWindow() {
        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(getActivity(), R.layout.share_pop_menu_layout, null);

        ImageView weibo = (ImageView) popView.findViewById(R.id.weibo);
        ImageView weixin = (ImageView) popView.findViewById(R.id.weixin);
        ImageView pengyouquan = (ImageView) popView.findViewById(R.id.pengyouquan);
        ImageView qq = (ImageView) popView.findViewById(R.id.qq);

        final PopupWindow popWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weibo:

                        break;
                    case R.id.weixin:
                        weiChat(0);
                        break;
                    case R.id.pengyouquan:
                        weiChat(1);
                        break;
                    case R.id.qq:

                        break;
                }
                popWindow.dismiss();
            }
        };

        weibo.setOnClickListener(listener);
        weixin.setOnClickListener(listener);
        pengyouquan.setOnClickListener(listener);
        qq.setOnClickListener(listener);

        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    // 0-分享给朋友  1-分享到朋友圈
    private void weiChat(int flag) {
        if (!api.isWXAppInstalled()) {
            showErrorMsg("您还未安装微信");
            return;
        }

        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.wobi365.com／";
        //创建一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "沃笔习字";
        msg.description = "规范汉语习字，弘扬民族文化";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求，这个必须有，否则会出错
        req.message = msg;

        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);
    }
}
