package com.wobi.android.wobiwriting.http;

import com.wobi.android.wobiwriting.http.callback.HttpCallback;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/9/24.
 */

public class OkHttpRequestImpl implements HttpRequestInterface {
    private static final String TAG = "OkHttpRequestImpl";
    private final DcsHttpManager dcsHttpManager;

    public OkHttpRequestImpl() {
        dcsHttpManager = DcsHttpManager.getInstance();
    }

    @Override
    public void doPostEventStringAsync(String bodyJson, HttpCallback callback) {
        LogUtil.d(TAG, "doPostEventStringAsync-bodyJson:" + bodyJson);
        if (HttpConfig.getBusinessServer() == null
                || HttpConfig.getBusinessServer().isEmpty()){
            LogUtil.d(TAG, " return since have no business address");
            return;
        }
        DcsHttpManager.post()
                .url(HttpConfig.getBusinessServer())
                .content(bodyJson)
                .mediaType(OkHttpMediaType.MEDIA_JSON_TYPE)
                .tag(HttpConfig.HTTP_EVENT_TAG)
                .build()
                .execute(callback);
    }

    @Override
    public void getBusinessServerAsync(String bodyJson, HttpCallback callback) {
        LogUtil.d(TAG, "getBusinessServerAsync-bodyJson:" + bodyJson);
        DcsHttpManager.post()
                .url(HttpConfig.getManagerServerUrl())
                .content(bodyJson)
                .mediaType(OkHttpMediaType.MEDIA_JSON_TYPE)
                .tag(HttpConfig.HTTP_MANAGER_TAG)
                .build()
                .execute(callback);
    }

    @Override
    public void doPingAsync(String bodyJson, HttpCallback callback) {
        LogUtil.d(TAG, "doGetPingAsync");
        DcsHttpManager.post()
                .url("")
                .tag(HttpConfig.HTTP_PING_TAG)
                .build()
                .execute(callback);
    }

    @Override
    public void cancelRequest(Object requestTag) {
        dcsHttpManager.cancelTag(requestTag);
    }
}