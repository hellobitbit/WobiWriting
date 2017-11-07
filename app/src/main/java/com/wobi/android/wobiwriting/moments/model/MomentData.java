package com.wobi.android.wobiwriting.moments.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wangyingren on 2017/11/2.
 */

public class MomentData implements Serializable{

    private CommunityInfo communityInfo;
    private Map<String, String> provinceMap;

    public void setCommunityInfo(CommunityInfo communityInfo){
        this.communityInfo = communityInfo;
    }

    public CommunityInfo getCommunityInfo(){
        return communityInfo;
    }

    public void setProvinceMap(Map<String, String> provinceMap){
        this.provinceMap = provinceMap;
    }

    public Map<String, String> getProvinceMap(){
        return provinceMap;
    }
}
