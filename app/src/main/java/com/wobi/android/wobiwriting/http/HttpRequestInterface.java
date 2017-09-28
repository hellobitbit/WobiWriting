package com.wobi.android.wobiwriting.http;

import com.wobi.android.wobiwriting.http.callback.HttpCallback;

/**
 * Created by wangyingren on 2017/9/24.
 */
public interface HttpRequestInterface {
    /**
     * 异步Post EventString请求
     *
     * @param bodyJson 请求信息体
     * @param callback 结果回调接口
     */
    void doPostEventStringAsync(String bodyJson, HttpCallback callback);

    /**
     * 异步Post EventString请求
     *
     * @param bodyJson 请求信息体
     * @param callback 结果回调接口
     */
    void getBusinessServerAsync(String bodyJson, HttpCallback callback);

    /**
     * 异步Get Ping请求
     *
     * @param bodyJson 请求信息体
     * @param callback 结果回调接口
     */
    void doPingAsync(String bodyJson, HttpCallback callback);

    /**
     * 取消请求
     *
     * @param requestTag 请求标识
     */
    void cancelRequest(Object requestTag);
}