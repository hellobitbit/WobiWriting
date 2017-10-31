package com.wobi.android.wobiwriting.moments.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.moments.model.Province;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class GetAllProvincesResponse extends Response {

   private List<Province> province_list;

    public List<Province> getProvince_list(){
        return province_list;
    }
}
