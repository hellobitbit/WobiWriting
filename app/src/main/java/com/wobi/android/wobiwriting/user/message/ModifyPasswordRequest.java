package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2018/5/9.
 */

public class ModifyPasswordRequest extends Request {

    private int user_id; //用户id
    private String old_password;
    private String new_password;

    public ModifyPasswordRequest(){
        setRequestType(BusinessType.BT_User_Change_Password.getValue());
        mInstance = this;
    }

    public void setUserId(int user_id){
        this.user_id = user_id;
    }

    public void setOldPassword(String old_password){
        this.old_password = old_password;
    }

    public void setNewPassword(String new_password){
        this.new_password = new_password;
    }
}
