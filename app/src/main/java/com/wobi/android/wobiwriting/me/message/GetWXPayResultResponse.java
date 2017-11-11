package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/11/10.
 */

public class GetWXPayResultResponse extends Response {

    private int pay_result;

    public int getPay_result(){
        return pay_result;
    }
}
