package com.wobi.android.wobiwriting.data.message;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.http.HttpConfig;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class Request {

    private static Gson gson = new Gson();
    protected Request mInstance;
    int request_type;
    String session_id;

    public void setRequestType(int request_type){
        this.request_type = request_type;
        if (HttpConfig.session_id !=null && !HttpConfig.session_id.isEmpty()){
            this.session_id = HttpConfig.session_id;
        }
    }

    public String jsonToString(){
        return gson.toJson(mInstance);
    }
}
