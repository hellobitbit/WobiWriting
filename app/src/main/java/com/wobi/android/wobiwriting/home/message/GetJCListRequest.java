package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetJCListRequest extends Request {

    public GetJCListRequest(){
        setRequestType(BusinessType.BT_Get_JC_List.getValue());
        mInstance = this;
    }
}
