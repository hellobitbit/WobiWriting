package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/24.
 */

public class JoinCommunityRequest extends Request {

    private int user_id;
    private int community_id;

    public JoinCommunityRequest(){
        setRequestType(BusinessType.BT_Join_Community.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setCommunity_id(int community_id){
        this.community_id = community_id;
    }
}
