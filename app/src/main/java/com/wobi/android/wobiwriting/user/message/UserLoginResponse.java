package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserLoginResponse extends Response {

    private int is_vip; // 0不是,1是

    private int login_result; // 登录结果,1为成功,2为失败

    public int getIsVip(){
        return is_vip;
    }

    public int getLoginResult(){
        return login_result;
    }
}
