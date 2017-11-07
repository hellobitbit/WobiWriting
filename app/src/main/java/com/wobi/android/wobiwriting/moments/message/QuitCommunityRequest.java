package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/2.
 */

public class QuitCommunityRequest extends Request {

    private int user_id;
    private int community_id;

    public QuitCommunityRequest(){
        setRequestType(BusinessType.BT_Quit_Community.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setCommunity_id(int community_id){
        this.community_id = community_id;
    }
}
