package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/26.
 */

public class UserChangeInfoRequest extends Request {

    private int user_id;
    private String user_name;
    private String password;
    private int sex;
    private String address;

    public UserChangeInfoRequest(){
        setRequestType(BusinessType.BT_User_Change_Info.getValue());
        mInstance = this;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setSex(int sex){
        this.sex = sex;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
