package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserLogoutRequest extends WithUserInfoRequest {

    public UserLogoutRequest(){
        setRequestType(BusinessType.BT_User_Logout.getValue());
        mInstance = this;
    }
}
