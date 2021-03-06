package com.wobi.android.wobiwriting.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.message.GetSCInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSCInfoResponse;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.me.MyVipActivity;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.ui.MainActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.user.RegisterActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.DateUtils;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/16.
 */

public class SearchActivity extends BaseActivity {
    public static final String SEARCH_TYPE = "search_type";
    private static final String TAG = "SearchActivity";
    private UserGetInfoResponse userInfoResponse;
    private EditText edit_search;
    private String type = "sz";
    private String[] defaultSzs = {"本", "一", "二", "三", "口", "目", "手"};
    private List<String> list = new ArrayList<>();

    private CustomDialog vipDialog;
    private DialogInterface mVipTipsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        type = getIntent().getStringExtra(SEARCH_TYPE);
        LogUtil.d(TAG," type = "+type);
        initViews();
        list = Arrays.asList(defaultSzs);
    }

    private void initViews() {
        TextView cancelSearch = (TextView)findViewById(R.id.cancelSearch);
        edit_search = (EditText)findViewById(R.id.edit_search);
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftware();
                finish();
            }
        });
        if ("sz".equals(type)){
            edit_search.setHint("请输入需要查找的字");
        }else if ("sc".equals(type)){
            edit_search.setHint("请输入需要查找的词");
        }else if ("key".equals(type)){
            edit_search.setHint("请输入需要查找的字或词");
        }

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if ("sz".equals(type)){
                        loadSZInfo(edit_search.getText().toString());
                    }else if ("sc".equals(type)){
                        loadScInfo(edit_search.getText().toString());
                    }else if ("key".equals(type)){
                        String key = edit_search.getText().toString();
                        LogUtil.d(TAG, "key length = "+key.length());
                        String userInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
                        LogUtil.d(TAG, " refreshLoginState userInfo == " + userInfo);
                        if (userInfo != null && !userInfo.isEmpty()) {
                            userInfoResponse = gson.fromJson(userInfo, UserGetInfoResponse.class);
                            if (userInfoResponse.getIs_vip() == 1
                                    && !MyVipActivity.isExpired(userInfoResponse.getVip_expire_time())){
                                if (key.length() > 1){
                                    loadScInfo(edit_search.getText().toString());
                                }else if (key.length() == 1){
                                    loadSZInfoExt(edit_search.getText().toString());
                                }
                            }else {
                                if (list.contains(key)){
                                    if (key.length() > 1){
                                        loadScInfo(edit_search.getText().toString());
                                    }else if (key.length() == 1){
                                        loadSZInfoExt(edit_search.getText().toString());
                                    }
                                }else {
                                    switchToVip();
                                }
                            }
                        }else {
                            checkLogin();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void loadSZInfoExt(final String text){
        GetSZInfoRequest request = new GetSZInfoRequest();
        request.setSz(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZInfoResponse szInfoResponse = gson.fromJson(response, GetSZInfoResponse.class);
                if (szInfoResponse != null && szInfoResponse.getHandleResult().equals("OK")){
                    if (!TextUtils.isEmpty(szInfoResponse.getWord())) {
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(text);
                        Intent intent = new Intent(SearchActivity.this, SearchSpeakCNActivity.class);
                        intent.putStringArrayListExtra(SearchSpeakCNActivity.SZ_LIST, list);
                        startActivity(intent);
                    }else {
                        loadScInfo(text);
                    }
                }else {
                    showErrorMsg("获取生字信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void loadSZInfo(final String text){
        GetSZInfoRequest request = new GetSZInfoRequest();
        request.setSz(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZInfoResponse szInfoResponse = gson.fromJson(response, GetSZInfoResponse.class);
                if (szInfoResponse != null && szInfoResponse.getHandleResult().equals("OK")){
                    if (!TextUtils.isEmpty(szInfoResponse.getWord())) {
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(text);
                        Intent intent = new Intent(SearchActivity.this, SearchSpeakCNActivity.class);
                        intent.putStringArrayListExtra(SearchSpeakCNActivity.SZ_LIST, list);
                        startActivity(intent);
                    }else {
                        showErrorMsg("没有该生字信息");
                    }
                }else {
                    showErrorMsg("获取生字信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void loadScInfo(final String text){
        GetSCInfoRequest request = new GetSCInfoRequest();
        request.setSc(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSCInfoResponse scInfoResponse = gson.fromJson(response, GetSCInfoResponse.class);
                if (scInfoResponse != null && scInfoResponse.getHandleResult().equals("OK")){
                    if (!TextUtils.isEmpty(scInfoResponse.getSc())) {
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(text);
                        Intent intent = new Intent(SearchActivity.this, SearchSpeakCNScActivity.class);
                        intent.putStringArrayListExtra(SearchSpeakCNScActivity.SZ_LIST, list);
                        startActivity(intent);
                    }else {
                        showErrorMsg("没有该生词信息");
                    }
                }else {
                    showErrorMsg("获取生词信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void hideSoftware(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void switchToVip() {
        if (vipDialog != null && vipDialog.isShowing()){
            return;
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.display_contentview_vip_layout, null);
        CustomDialog.Builder builder = new CustomDialog.Builder(SearchActivity.this);
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setContentView(contentView);
        builder.setPositiveButton("成为VIP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(SearchActivity.this, MyVipActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("考虑一下",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }


    protected void checkLogin(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("登录后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }
}
