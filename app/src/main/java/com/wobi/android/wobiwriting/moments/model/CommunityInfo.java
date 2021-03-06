package com.wobi.android.wobiwriting.moments.model;

import android.support.annotation.NonNull;

import com.wobi.android.wobiwriting.utils.DateUtils;

import java.io.Serializable;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CommunityInfo implements Serializable, Comparable<CommunityInfo>{

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
    private String joined_time = "1970-01-01 00:00:00";

    public void setAddress(String address){
        this.address = address;
    }

    public void setCity_code(String city_code){
        this.city_code = city_code;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public void setIs_auth(int is_auth){
        this.is_auth = is_auth;
    }

    public void setCommunity_name(String community_name){
        this.community_name = community_name;
    }

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

    public void setJoin_community_time(String joined_time){
        this.joined_time = joined_time;
    }

    public String getJoin_community_time(){
        return joined_time;
    }

    @Override
    public int compareTo(@NonNull CommunityInfo o) {

        return DateUtils.compare(this.joined_time, o.getJoin_community_time());
    }
}
