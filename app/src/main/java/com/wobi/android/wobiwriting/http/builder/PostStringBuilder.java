package com.wobi.android.wobiwriting.http.builder;

import com.wobi.android.wobiwriting.http.request.PostStringRequest;
import com.wobi.android.wobiwriting.http.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by wangyingren on 2017/9/24.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    // 内容
    private String content;
    // 类型
    private MediaType mediaType;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType, id).build();
    }
}