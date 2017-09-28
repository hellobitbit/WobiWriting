package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserRegisterBaseRequest extends Request {

    private String phone_number;

    public void setPhoneNumber(String phone_number){
        this.phone_number = phone_number;
    }
}
