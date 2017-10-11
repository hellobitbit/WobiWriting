package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class SanZiJingRequest extends Request {

    public SanZiJingRequest(){
        setRequestType(BusinessType.BT_San_Zi_Jing.getValue());
        mInstance = this;
    }
}
