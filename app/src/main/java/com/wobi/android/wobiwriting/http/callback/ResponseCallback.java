package com.wobi.android.wobiwriting.http.callback;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/24.
 */
public class ResponseCallback extends HttpCallback<Response> {
    private static final String TAG = "ResponseCallback";

    @Override
    public Response parseNetworkResponse(Response response, int id) throws Exception {
        return response;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d(TAG, "onError:" + e.getMessage());
    }

    @Override
    public void onResponse(Response response, int id) {
        Log.d(TAG, "onResponse:" + response.code());
    }
}