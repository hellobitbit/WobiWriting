package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserVerifyPhoneResponse extends Response {

    // 1该手机号码未注册,2该手机号码已经注册,3出现异常情况
    private int phone_status;

    public int getPhoneStatus(){
        return phone_status;
    }
}
