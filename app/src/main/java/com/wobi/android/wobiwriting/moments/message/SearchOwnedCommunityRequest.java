package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/26.
 */

public class SearchOwnedCommunityRequest extends Request {

    private int user_id;

    public SearchOwnedCommunityRequest(){
        setRequestType(BusinessType.BT_Search_Owned_Community.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
}
