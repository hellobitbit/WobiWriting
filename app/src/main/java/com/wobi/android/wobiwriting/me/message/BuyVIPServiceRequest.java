package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/11/10.
 */

public class BuyVIPServiceRequest extends Request {

    private int user_id;
    private String request_code;
    private double cost;
    private double wobi_beans_cost;
    private int time_limit;

    public BuyVIPServiceRequest(){
        setRequestType(BusinessType.BT_Buy_VIP_Service.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setRequest_code(String request_code){
        this.request_code = request_code;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public void setWobi_beans_cost(double wobi_beans_cost){
        this.wobi_beans_cost = wobi_beans_cost;
    }

    public void setTime_limit(int time_limit){
        this.time_limit = time_limit;
    }
}

