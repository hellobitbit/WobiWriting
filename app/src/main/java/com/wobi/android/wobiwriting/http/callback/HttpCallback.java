package com.wobi.android.wobiwriting.http.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/24.
 */
public abstract class HttpCallback<T> {
    /**
     * UI线程
     *
     * @param request 请求
     * @param id      请求id
     */
    public void onBefore(Request request, int id) {
    }

    /**
     * UI线程
     *
     * @param id 请求id
     */
    public void onAfter(int id) {
    }

    public static HttpCallback backDefaultCallBack = new HttpCallback() {

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };

    public boolean validateResponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * 非UI线程
     *
     * @param response response
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;

    /**
     * 错误处理，UI线程
     *
     * @param call call
     * @param e    e  错误异常信息
     * @param id   id 请求id
     */
    public abstract void onError(Call call, Exception e, int id);

    /**
     * 数据回调，UI线程
     *
     * @param response response
     * @param id       id
     */
    public abstract void onResponse(T response, int id);
}