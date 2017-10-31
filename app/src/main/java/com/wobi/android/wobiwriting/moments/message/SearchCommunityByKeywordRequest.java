package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class SearchCommunityByKeywordRequest extends Request {

    private String keyword;

    public SearchCommunityByKeywordRequest(){
        setRequestType(BusinessType.BT_Search_Community_By_Keyword.getValue());
        mInstance = this;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
}
