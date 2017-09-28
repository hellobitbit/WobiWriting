package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class ShuFaKeTangMBRequest extends ShuFaKeTangRequest {

    public ShuFaKeTangMBRequest(){
        setRequestType(BusinessType.BT_Shu_Fa_Ke_Tang_MB.getValue());
    }
}
