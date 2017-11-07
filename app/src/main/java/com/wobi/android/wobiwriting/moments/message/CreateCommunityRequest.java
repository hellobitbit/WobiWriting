package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/31.
 */

public class CreateCommunityRequest extends Request {

    private int user_id;
    private String community_name;
    private String address;
    private String city_code;
    private int is_alive;
    private int is_auth;
    private String summary;

    public CreateCommunityRequest(){
        setRequestType(BusinessType.BT_Create_Community.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setCommunity_name(String community_name){
        this.community_name = community_name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setCity_code(String city_code){
        this.city_code = city_code;
    }

    public void setIs_alive(int is_alive){
        this.is_alive = is_alive;
    }

    public void setIs_auth(int is_auth){
        this.is_auth = is_auth;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }
}


