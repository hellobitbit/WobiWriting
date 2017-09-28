package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserGetVerifyCodeRequest extends UserRegisterBaseRequest {

    public UserGetVerifyCodeRequest(){
        setRequestType(BusinessType.BT_User_Get_Verify_Code.getValue());
    }
}
