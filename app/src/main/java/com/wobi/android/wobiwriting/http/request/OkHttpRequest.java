package com.wobi.android.wobiwriting.http.request;

import com.wobi.android.wobiwriting.http.callback.HttpCallback;
import com.wobi.android.wobiwriting.http.exceptions.Exceptions;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wangyingren on 2017/9/28.
 */
public abstract class OkHttpRequest {
    // url
    protected String url;
    // 用于取消请求的tag
    protected Object tag;
    // 请求参数
    protected Map<String, String> params;
    // 请求header
    protected Map<String, String> headers;
    // 请求的标识id
    protected int id;
    // okhttp的请求构建类
    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params,
                            Map<String, String> headers,
                            int id) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.id = id;
        if (url == null) {
            Exceptions.illegalArgument("url can not be null.");
        }
        initBuilder();
    }

    /**
     * 初始化okhttp的请求builder
     */
    private void initBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }

    /**
     * 拼接请求的Header
     */
    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public int getId() {
        return id;
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    public Request generateRequest(HttpCallback dcsCallback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, dcsCallback);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final HttpCallback dcsCallback) {
        return requestBody;
    }

    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);
}
