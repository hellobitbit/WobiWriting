package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/11/29.
 */

public class GetSCInfoResponse extends Response {

    private String banshu_url;
    private String dz_banshu_url;
    private String dz_maobi_url;
    private String dz_swjz_url;
    private String dz_yingbi_url;
    private String jieshi_url;
    private String maobi_url;
    private String pinyin1;
    private String pinyin2;
    private String pinyin3;
    private String pinyin4;
    private String pretation;
    private String yingbi_url;
    private String sc;

    public String getBanshu_url(){
        return banshu_url;
    }

    public String getDz_banshu_url(){
        return dz_banshu_url;
    }

    public String getDz_maobi_url(){
        return dz_maobi_url;
    }

    public String getDz_swjz_url(){
        return dz_swjz_url;
    }

    public String getDz_yingbi_url(){
        return dz_yingbi_url;
    }

    public String getJieshi_url(){
        return jieshi_url;
    }

    public String getMaobi_url(){
        return maobi_url;
    }

    public String getPinyin1(){
        return pinyin1;
    }

    public String getPinyin2(){
        return pinyin2;
    }

    public String getPinyin3(){
        return pinyin3;
    }

    public String getPinyin4(){
        return pinyin4;
    }

    public String getPretation(){
        return pretation;
    }

    public String getYingbi_url(){
        return yingbi_url;
    }

    public String getSc(){
        return sc;
    }
}

