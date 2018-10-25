package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2018/10/22.
 */

public class VIPLIstRequest extends Request {

    public VIPLIstRequest(){
        setRequestType(BusinessType.BT_GET_VIP_LIST.getValue());
        mInstance = this;
    }
}
