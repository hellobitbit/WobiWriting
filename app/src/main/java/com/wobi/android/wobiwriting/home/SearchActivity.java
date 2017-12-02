package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.message.GetSCInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSCInfoResponse;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by wangyingren on 2017/10/16.
 */

public class SearchActivity extends BaseActivity {
    public static final String SEARCH_TYPE = "search_type";
    private static final String TAG = "SearchActivity";
    private EditText edit_search;
    private String type = "sz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        type = getIntent().getStringExtra(SEARCH_TYPE);
        LogUtil.d(TAG," type = "+type);
        initViews();
    }

    private void initViews() {
        TextView cancelSearch = (TextView)findViewById(R.id.cancelSearch);
        edit_search = (EditText)findViewById(R.id.edit_search);
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("sz".equals(type)){
            edit_search.setHint("请输入需要查找的字");
        }else if ("sc".equals(type)){
            edit_search.setHint("请输入需要查找的词");
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
                    }

                }
                return false;
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
                showNetWorkException();
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
                showNetWorkException();
            }
        });
    }
}
