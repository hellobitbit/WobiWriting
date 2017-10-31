package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class GetAllProvincesRequest extends Request {

    public GetAllProvincesRequest(){
        setRequestType(BusinessType.BT_Get_Province.getValue());
        mInstance = this;
    }
}
