package com.wobi.android.wobiwriting.moments.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/12/24.
 */

public class CommunityInfos {

    private List<CommunityInfoForPurchase> communityInfos;

    public List<CommunityInfoForPurchase> getCommunityInfos(){
        return communityInfos;
    }

    public void initCommunityInfos(){
        communityInfos = new ArrayList<>();
    }

    public boolean isContains(CommunityInfoForPurchase communityInfoForPurchase){
        for (CommunityInfoForPurchase infoForPurchase: communityInfos){

            if (infoForPurchase.getRequest_code().equals(communityInfoForPurchase.getRequest_code())){
                return true;
            }
        }

        return false;
    }

    public void updateCommunityInfo(CommunityInfoForPurchase infoForPurchase){
        for (CommunityInfoForPurchase info: communityInfos){

            if (info.getRequest_code().equals(infoForPurchase.getRequest_code())){
                info.setJoin_community_time(infoForPurchase.getJoin_community_time());
            }
        }
    }

    public void deleteCommunityInfo(String request_code){
        CommunityInfoForPurchase needDelete = null;
        for (CommunityInfoForPurchase info: communityInfos){

            if (info.getRequest_code().equals(request_code)){
                needDelete = info;
                break;
            }
        }
        communityInfos.remove(needDelete);
    }
}
