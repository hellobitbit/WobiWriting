package com.wobi.android.wobiwriting.upgrade.model;

/**
 * Created by wangyingren on 2018/1/13.
 */

public class LatestAppVersion {

    private int force_update;
    private int id;
    private int platform;
    private String pub_time;
    private String remark;
    private String soft_name;
    private String soft_url;
    private String ver_name;
    private int ver_num;

    public String getPub_time(){
        return pub_time;
    }

    public String getRemark(){
        return remark;
    }

    public String getSoft_name(){
        return soft_name;
    }

    public String getSoft_url(){
        return soft_url;
    }

    public String getVer_name(){
        return ver_name;
    }

    public int getForce_update(){
        return force_update;
    }

    public int getId(){
        return id;
    }

    public int getPlatform(){
        return platform;
    }

    public int getVer_num(){
        return ver_num;
    }
}
