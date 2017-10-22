package com.wobi.android.wobiwriting.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.me.FeedbackActivity;
import com.wobi.android.wobiwriting.me.MyFollowActivity;
import com.wobi.android.wobiwriting.me.MyMomentActivity;
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
    private ImageView user_icon;
    private TextView user_name;
    private TextView user_description;
    private TextView moments_num_value;
    private TextView follow_num_value;
    private TextView wodou_num_value;
    private UserGetInfoResponse userInfoResponse;

    private DialogInterface mLoginTipsDialog;

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
                break;
            case R.id.user_info:
                Intent personalInfo = new Intent(getActivity(), MyInformationActivity.class);
                getActivity().startActivity(personalInfo);
                break;
            case R.id.moments_num_layout:
                Intent moments = new Intent(getActivity(), MyMomentActivity.class);
                getActivity().startActivity(moments);
                break;
            case R.id.follow_num_layout:
                Intent follow = new Intent(getActivity(), MyFollowActivity.class);
                getActivity().startActivity(follow);
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
        request.setUserId(Integer.parseInt(userInfoResponse.getUserId()));
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
//                    dialog.dismiss();
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
}
