package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/10.
 */

public class SearchCommunityByNameRequest extends Request {

    private String keyword;

    public SearchCommunityByNameRequest(){
        setRequestType(BusinessType.BT_Search_Community_By_Name.getValue());
        mInstance = this;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
}
