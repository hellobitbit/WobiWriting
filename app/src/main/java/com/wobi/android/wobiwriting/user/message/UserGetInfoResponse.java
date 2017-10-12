package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class UserGetInfoResponse extends Response {

    private String phone_number;//用户手机，用户名

    private String name; //用户名字

    private int sex;   //0为男 1是女

    private String city_code; //区域代码

    private String address; //详细地址

    private String request_code; //邀请码

    private int wobi_beans; //沃笔豆豆的数量

    private int user_amount; //用户账户余额（只限圈主）

    private int community_count; // 加入和创建的圈子的总数

    public String getPhoneNumber(){
        return phone_number;
    }

    public String getName(){
        return name;
    }

    public int getSex(){
        return sex;
    }

    public String getCityCode(){
        return city_code;
    }

    public String getAddress(){
        return address;
    }

    public String getRequestCode(){
        return request_code;
    }

    public int getWobiBeans(){
        return wobi_beans;
    }

    public int getUserAmount(){
        return user_amount;
    }

    public int getCommunityCount(){
        return community_count;
    }
}