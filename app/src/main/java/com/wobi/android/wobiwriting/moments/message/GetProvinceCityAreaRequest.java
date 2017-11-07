package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/7.
 */

public class GetProvinceCityAreaRequest extends Request {

    public GetProvinceCityAreaRequest(){
        setRequestType(BusinessType.BT_Get_Province_City_Area.getValue());
        mInstance = this;
    }
}
