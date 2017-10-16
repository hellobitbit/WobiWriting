package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class ShuFaKeTangYBRequest extends ShuFaKeTangRequest {

    public ShuFaKeTangYBRequest(){
        setRequestType(BusinessType.BT_Shu_Fa_Ke_Tang_YB.getValue());
        mInstance = this;
    }
}
