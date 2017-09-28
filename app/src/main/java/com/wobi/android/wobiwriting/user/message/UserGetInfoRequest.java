package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserGetInfoRequest extends Request {

    private String user_id; //用户id

    public UserGetInfoRequest(){
        setRequestType(BusinessType.BT_User_Get_Info.getValue());
    }

    public void setUserId(String user_id){
        this.user_id = user_id;
    }
}
