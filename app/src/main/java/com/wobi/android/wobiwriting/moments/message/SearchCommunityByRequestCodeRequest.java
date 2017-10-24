package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/24.
 */

public class SearchCommunityByRequestCodeRequest extends Request {

    private String request_code;

    public SearchCommunityByRequestCodeRequest(){
        setRequestType(BusinessType.BT_Search_Community_By_Request_Code.getValue());
        mInstance = this;
    }

    public void setRequest_code(String request_code){
        this.request_code = request_code;
    }
}
