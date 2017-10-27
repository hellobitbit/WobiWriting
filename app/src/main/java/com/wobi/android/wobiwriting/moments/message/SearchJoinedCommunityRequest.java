package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/27.
 */

public class SearchJoinedCommunityRequest extends Request {

    private int user_id;

    public SearchJoinedCommunityRequest(){
        setRequestType(BusinessType.BT_Search_Joined_Community.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
}
