package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserLoginRequest extends UserRegisterBaseRequest {

    private String password;

    public UserLoginRequest(){
        setRequestType(BusinessType.BT_User_Login.getValue());
    }

    public void setPassword(String password){
        this.password = password;
    }
}
