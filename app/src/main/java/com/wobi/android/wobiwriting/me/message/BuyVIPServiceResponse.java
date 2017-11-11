package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/11/10.
 */

public class BuyVIPServiceResponse extends Response {

    private String appid;
    private double cost;
    private String noncestr;
    private int order_result;
    private String partnerid;
    private String prepayid;
    private String sign;
    private int time_limit;
    private String timestamp;

    public String getAppid(){
        return appid;
    }

    public double getCost(){
        return cost;
    }

    public String getNoncestr(){
        return noncestr;
    }

    public int getOrder_result(){
        return order_result;
    }

    public String getPartnerid(){
        return partnerid;
    }

    public String getPrepayid(){
        return prepayid;
    }

    public String getSign(){
        return sign;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public int getTime_limit(){
        return time_limit;
    }
}
