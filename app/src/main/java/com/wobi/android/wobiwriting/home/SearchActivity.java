package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
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
    private static final String TAG = "SearchActivity";
    private EditText edit_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
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

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadSZInfo(edit_search.getText().toString());
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
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(text);
                    Intent intent = new Intent(SearchActivity.this, SearchSpeakCNActivity.class);
                    intent.putStringArrayListExtra(SearchSpeakCNActivity.SZ_LIST, list);
                    startActivity(intent);
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
}
