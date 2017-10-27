package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserLoginResponse extends Response {

    private boolean is_vip; // 0不是,1是

    private int login_result; // 登录结果,1为成功,2为失败

    private String session_id;

    private String user_id;// 用户的唯一ID

    public boolean getIsVip(){
        return is_vip;
    }

    public int getLoginResult(){
        return login_result;
    }

    public String getSession_id(){
        return session_id;
    }

    public String getUserId(){
        return user_id;
    }
}
