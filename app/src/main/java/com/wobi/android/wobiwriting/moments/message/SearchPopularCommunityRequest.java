package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class SearchPopularCommunityRequest extends Request {

    public SearchPopularCommunityRequest(){
        setRequestType(BusinessType.BT_Search_Popular_Community.getValue());
        mInstance = this;
    }
}
