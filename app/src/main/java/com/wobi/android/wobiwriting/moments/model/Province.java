package com.wobi.android.wobiwriting.moments.model;

import java.io.Serializable;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class Province implements Serializable{

    private String city_code;
    private int id;
    private String province_name;

    public String getCity_code(){
        return city_code;
    }

    public int getId(){
        return id;
    }

    public String getProvince_name(){
        return province_name;
    }

}
