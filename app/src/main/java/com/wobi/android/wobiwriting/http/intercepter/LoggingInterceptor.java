package com.wobi.android.wobiwriting.http.intercepter;

import com.wobi.android.wobiwriting.utils.LogUtil;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/24.
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "HttpLog";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogUtil.d(TAG, String.format("request: %s [%s]",
                request.method(), request.url()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        LogUtil.d(TAG, String.format(Locale.CANADA, "response: %d [%s] %.1fms%n%s",
                response.code(), response.request().url(), (t2 - t1) / 1e6d, ""));
        return response;
    }
}