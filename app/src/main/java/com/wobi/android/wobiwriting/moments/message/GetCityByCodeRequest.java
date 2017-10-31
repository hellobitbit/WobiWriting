package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class GetCityByCodeRequest extends Request {

    private String city_code;

    public GetCityByCodeRequest(){
        setRequestType(BusinessType.BT_Get_City.getValue());
        mInstance = this;
    }

    public void setCity_code(String city_code){
        this.city_code = city_code;
    }
}
