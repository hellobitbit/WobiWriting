package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserLoginResponse extends Response {

    private int is_vip; // 0不是,1是

    private String address;

    private String city_code;

    private String name;

    private String phone_number;

    private String request_code;

    private int login_result; // 登录结果,1为成功,2为失败

    private String session_id;

    private String user_id;// 用户的唯一ID

    private int sex;

    private int wobi_beans;

    public int getIsVip(){
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

    public String getAddress(){
        return address;
    }

    public String getCity_code(){
        return city_code;
    }

    public String getName(){
        return name;
    }

    public String getPhone_number(){
        return phone_number;
    }

    public String getRequest_code(){
        return request_code;
    }

    public int getSex(){
        return sex;
    }

    public int getWobi_beans(){
        return wobi_beans;
    }
}

