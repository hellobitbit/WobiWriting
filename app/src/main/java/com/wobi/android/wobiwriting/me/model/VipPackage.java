package com.wobi.android.wobiwriting.me.model;

/**
 * Created by wangyingren on 2018/10/22.
 */

public class VipPackage {

    private int id;
    private int valid_months;
    private float price;
    private int type;
    private String remarks;

    public int getId(){
        return id;
    }

    public int getValid_months(){
        return valid_months;
    }

    public float getPrice(){
        return price;
    }

    public int getType(){
        return type;
    }

    public String getRemarks(){
        return remarks;
    }
}
