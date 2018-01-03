package com.wobi.android.wobiwriting.moments.model;

/**
 * Created by wangyingren on 2017/12/24.
 */

public class JoinMomentObj {

    private String join_community_time;
    private String request_code;

    public void setJoin_community_time(String join_community_time){
        this.join_community_time = join_community_time;
    }

    public String getJoin_community_time(){
        return join_community_time;
    }

    public String getRequest_code(){
        return request_code;
    }

    public void setrequest_code(String request_code){
        this.request_code = request_code;
    }
}
