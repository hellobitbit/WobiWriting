package com.wobi.android.wobiwriting.user.message;

import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class WithUserInfoRequest extends Request {

    private int user_id; // 用户的唯一id

    public void setUserId(int user_id){
        this.user_id = user_id;
    }
}
