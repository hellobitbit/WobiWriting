package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserCommitRegisterInfoRequest extends UserRegisterBaseRequest {


    private String password;

    private String request_code;

    public UserCommitRegisterInfoRequest(){
        setRequestType(BusinessType.BT_User_Commit_Register_Info.getValue());
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRequestCode(String request_code){
        this.request_code = request_code;
    }
}
