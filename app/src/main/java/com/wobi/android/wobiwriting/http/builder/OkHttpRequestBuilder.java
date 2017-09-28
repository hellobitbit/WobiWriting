package com.wobi.android.wobiwriting.http.builder;

import com.wobi.android.wobiwriting.http.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wangyingren on 2017/9/24.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    // 请求url
    protected String url;
    // 请求tag
    protected Object tag;
    // 请求的headers
    protected Map<String, String> headers;
    // 请求的参数集合
    protected Map<String, String> params;
    // 请求的表识id
    protected int id;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    /**
     * 构建请求
     *
     * @return RequestCall
     */
    public abstract RequestCall build();
}