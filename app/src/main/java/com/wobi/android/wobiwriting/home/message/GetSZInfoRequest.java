package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetSZInfoRequest extends Request {

    private String sz;

    public GetSZInfoRequest(){
        setRequestType(BusinessType.BT_Get_SZ_Info.getValue());
    }

    public void setSz(String sz){
        this.sz = sz;
    }
}
