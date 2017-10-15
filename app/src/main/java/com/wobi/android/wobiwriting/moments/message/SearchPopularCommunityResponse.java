package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class SearchPopularCommunityResponse extends Response {

    private List<CommunityInfo> community_list;

    public List<CommunityInfo> getCommunityList(){
        return community_list;
    }
}
