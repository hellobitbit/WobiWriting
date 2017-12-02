package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/29.
 */

public class GetSCInfoRequest extends Request {

    private String sc;

    public GetSCInfoRequest(){
        setRequestType(BusinessType.BT_Get_Sc_Info.getValue());
        mInstance = this;
    }

    public void setSc(String sc){
        this.sc = sc;
    }
}
