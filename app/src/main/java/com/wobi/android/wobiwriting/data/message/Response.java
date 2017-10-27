package com.wobi.android.wobiwriting.data.message;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class Response {

    private int request_type;
    private String handle_result;
//    private String user_id;// 用户的唯一ID

    public int getRequestType(){
        return this.request_type;
    }

    public String getHandleResult(){
        return this.handle_result;
    }

//    public String getUserId(){
//        return user_id;
//    }
}
