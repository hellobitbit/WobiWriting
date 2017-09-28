package com.wobi.android.wobiwriting.http.request;

import com.wobi.android.wobiwriting.http.DcsHttpManager;
import com.wobi.android.wobiwriting.http.callback.HttpCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by wangyingren on 2017/9/28.
 */
public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;
    private OkHttpClient clone;
    // 读取超时时间
    private long readTimeOut;
    // 写入数据超时时间
    private long writeTimeOut;
    // 连接超时时间
    private long connTimeOut;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    /**
     * 构建请求
     *
     * @param dcsCallback 回调
     * @return Call
     */
    public Call buildCall(HttpCallback dcsCallback) {
        request = generateRequest(dcsCallback);
        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : DcsHttpManager.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : DcsHttpManager.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : DcsHttpManager.DEFAULT_MILLISECONDS;
            clone = DcsHttpManager.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else {
            call = DcsHttpManager.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(HttpCallback dcsCallback) {
        return okHttpRequest.generateRequest(dcsCallback);
    }

    public void execute(HttpCallback dcsCallback) {
        buildCall(dcsCallback);
        if (dcsCallback != null) {
            dcsCallback.onBefore(request, getOkHttpRequest().getId());
        }
        DcsHttpManager.getInstance().execute(this, dcsCallback);
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Call getCall() {
        return call;
    }
}