package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class CreateCommunityResponse extends Response {

    private int community_id;
    private int is_alive;
    private int create_result;
    private String request_code;

    public int getCommunity_id(){
        return community_id;
    }

    public int getIs_alive(){
        return is_alive;
    }

    public int getCreate_result(){
        return create_result;
    }

    public String getRequest_code(){
        return request_code;
    }
}
