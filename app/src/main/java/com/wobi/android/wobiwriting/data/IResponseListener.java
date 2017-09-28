package com.wobi.android.wobiwriting.data;

import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/24.
 */

public interface IResponseListener {

    /**
     * 成功回调
     *
     * @param response http返回json String
     */
    void onSucceed(String response);

    /**
     * 失败回调
     *
     * @param errorMessage 出错的异常信息
     */
    void onFailed(String errorMessage);
}
