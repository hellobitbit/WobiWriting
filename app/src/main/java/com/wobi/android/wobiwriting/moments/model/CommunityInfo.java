package com.wobi.android.wobiwriting.moments.model;

import java.io.Serializable;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CommunityInfo implements Serializable{

    private String address;
    private String city_code;
    private String community_name;
    private String create_time;
    private String head_pic;
    private int id;
    private int is_alive;
    private int is_auth;
    private String request_code;
    private String summary;
    private int user_id;

    public String getAddress(){
        return address;
    }

    public String getCity_code(){
        return city_code;
    }

    public String getCommunity_name(){
        return community_name;
    }

    public String getCreate_time(){
        return create_time;
    }

    public String getHead_pic(){
        return head_pic;
    }

    public int getId(){
        return id;
    }

    public int getIs_alive(){
        return  is_alive;
    }

    public int getIs_auth(){
        return is_auth;
    }

    public String getRequest_code(){
        return request_code;
    }

    public String getSummary(){
        return summary;
    }

    public int getUser_id(){
        return user_id;
    }
}
