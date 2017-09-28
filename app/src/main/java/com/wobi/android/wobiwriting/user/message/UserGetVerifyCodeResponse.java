package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserGetVerifyCodeResponse extends Response {

    // 手机验证码
    private String verify_code;
    // 1验证码获取成功,2手机欠费等等……
    private int result;

    public String getVerifyCode(){
        return verify_code;
    }

    public int getResult(){
        return result;
    }
}
