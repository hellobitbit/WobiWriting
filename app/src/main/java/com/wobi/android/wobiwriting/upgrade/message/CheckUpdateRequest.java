package com.wobi.android.wobiwriting.upgrade.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2018/1/13.
 */

public class CheckUpdateRequest extends Request {

    private int platform;

    public CheckUpdateRequest(){
        setRequestType(BusinessType.BT_Check_App_Upgrade.getValue());
        mInstance = this;
    }

    public void setPlatform(int platform){
        this.platform = platform;
    }
}
