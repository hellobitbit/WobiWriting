package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/7.
 */

public class ChangeCommunityInfoRequest extends Request {

    private int community_id;
    private String community_name;
    private String address;
    private String city_code;
    private int is_auth;
    private String summary;

    public ChangeCommunityInfoRequest(){
        setRequestType(BusinessType.BT_Change_Community_Info.getValue());
        mInstance = this;
    }

    public void setCommunity_id(int community_id){
        this.community_id = community_id;
    }

    public void setCommunity_name(String community_name){
        this.community_name = community_name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setIs_auth(int is_auth){
        this.is_auth = is_auth;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public void setCity_code(String city_code){
        this.city_code = city_code;
    }
}
