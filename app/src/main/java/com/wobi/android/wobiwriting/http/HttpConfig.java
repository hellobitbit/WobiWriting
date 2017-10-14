package com.wobi.android.wobiwriting.http;

/**
 * Created by wangyingren on 2017/9/27.
 */
public class HttpConfig {
    // 请求event事件TAG
    public static final String HTTP_EVENT_TAG = "event";
    // 请求ping的TAG
    public static final String HTTP_PING_TAG = "ping";
    //请求manager事件TAG
    public static final String HTTP_MANAGER_TAG = "manager";

    public static final String  MANAGER_SERVER_URL = "http://114.55.92.149/manager";

    public static String getManagerServerUrl(){
        return MANAGER_SERVER_URL;
    }

    public static String business_server = "";

    public static String getBusinessServer(){
        return business_server;
    }

    public static void setBusinessServer(String url){
        business_server = url;
    }


    public static String session_id = "";

    public static String getSessionId() {
        return session_id;
    }

    public static void setSessionId(String session_id) {
        HttpConfig.session_id = session_id;
    }
}