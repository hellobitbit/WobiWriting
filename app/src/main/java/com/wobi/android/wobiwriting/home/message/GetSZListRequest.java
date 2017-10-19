package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetSZListRequest extends Request {

    private String kw_url; // 课文url

    private int jc_id; // 教材id

    public GetSZListRequest(){
        setRequestType(BusinessType.BT_Get_SZ_List.getValue());
        mInstance = this;
    }

    public void setKwUrl(String kw_url){
        this.kw_url = kw_url;
    }

    public void setJcId(int jc_id){
        this.jc_id = jc_id;
    }
}
