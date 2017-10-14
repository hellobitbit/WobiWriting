package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class UserVerifyPhoneRequest extends UserRegisterBaseRequest {

    public UserVerifyPhoneRequest(){
        setRequestType(BusinessType.BT_User_Verify_Phone.getValue());
        mInstance = this;
    }
}
