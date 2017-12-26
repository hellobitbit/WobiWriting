package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/12/24.
 */

public class JoinCommunityByRequestCodeRequest extends Request {
    private int user_id;
    private String request_code;

    public JoinCommunityByRequestCodeRequest(){
        setRequestType(BusinessType.BT_Join_Community_By_RequestCode.getValue());
        mInstance = this;
    }

    public void setRequest_code(String request_code){
        this.request_code = request_code;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

}
