package com.wobi.android.wobiwriting.http.builder;

import java.util.Map;


/**
 * Created by wangyingren on 2017/9/24.
 */
public interface HasParamInterface {
    /**
     * 构建请求参数
     *
     * @param params 请求参数
     * @return OkHttpRequestBuilder
     */
    OkHttpRequestBuilder params(Map<String, String> params);

    /**
     * @param key 请求参数的key
     * @param val 请求参数的值
     * @return OkHttpRequestBuilder
     */
    OkHttpRequestBuilder addParams(String key, String val);
}