package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class DiZiGuiRequest extends Request {

    public DiZiGuiRequest(){
        setRequestType(BusinessType.BT_Di_Zi_Gui.getValue());
    }
}
