package com.wobi.android.wobiwriting.data.message;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/25.
 */

public class ConnManagerRequest extends Request {

    public ConnManagerRequest(){
        setRequestType(BusinessType.BT_ConnManager.getValue());
        mInstance =  this;
    }

}
