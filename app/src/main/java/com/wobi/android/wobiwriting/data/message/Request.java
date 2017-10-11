package com.wobi.android.wobiwriting.data.message;

import com.google.gson.Gson;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class Request {

    private static Gson gson = new Gson();
    protected Request mInstance;
    int request_type;

    public void setRequestType(int request_type){
        this.request_type = request_type;
    }

    public String jsonToString(){
        return gson.toJson(mInstance);
    }
}
