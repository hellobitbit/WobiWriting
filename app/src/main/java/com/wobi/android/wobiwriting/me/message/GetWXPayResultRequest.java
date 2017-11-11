package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/10.
 */

public class GetWXPayResultRequest extends Request {

    private String prepayid; // 微信生成的预支付标识

    public GetWXPayResultRequest(){
        setRequestType(BusinessType.BT_Get_WXPay_Result.getValue());
        mInstance = this;
    }

    public void setPrepayid(String prepayid){
        this.prepayid = prepayid;
    }
}
