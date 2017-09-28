package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetGradeRequest extends Request {

    public GetGradeRequest(){
        setRequestType(BusinessType.BT_Get_Grade.getValue());
    }
}
