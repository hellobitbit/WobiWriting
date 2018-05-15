package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2018/5/9.
 */

public class ModifyPasswordRequest extends Request {

    private int user_id; //用户id
    private String password;

    public ModifyPasswordRequest(){
        setRequestType(BusinessType.BT_User_Change_Password.getValue());
        mInstance = this;
    }

    public void setUserId(int user_id){
        this.user_id = user_id;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
