package com.wobi.android.wobiwriting.moments.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/12/24.
 */

public class CommunityInfos {

    private List<JoinMomentObj> communityInfos;

    public List<JoinMomentObj> getCommunityInfos(){
        return communityInfos;
    }

    public void initCommunityInfos(){
        communityInfos = new ArrayList<>();
    }

    public boolean isContains(JoinMomentObj communityInfoForPurchase){
        for (JoinMomentObj infoForPurchase: communityInfos){

            if (infoForPurchase.getRequest_code().equals(communityInfoForPurchase.getRequest_code())){
                return true;
            }
        }

        return false;
    }

    public void updateInfo(CommunityInfo info){
        for (JoinMomentObj joinMomentObj: communityInfos){

            if (joinMomentObj.getRequest_code().equals(info.getRequest_code())){
                info.setJoin_community_time(joinMomentObj.getJoin_community_time());
            }
        }
    }

    public void updateCommunityInfo(JoinMomentObj infoForPurchase){
        for (JoinMomentObj info: communityInfos){

            if (info.getRequest_code().equals(infoForPurchase.getRequest_code())){
                info.setJoin_community_time(infoForPurchase.getJoin_community_time());
            }
        }
    }

    public void deleteCommunityInfo(String request_code){
        JoinMomentObj needDelete = null;
        for (JoinMomentObj info: communityInfos){

            if (info.getRequest_code().equals(request_code)){
                needDelete = info;
                break;
            }
        }
        communityInfos.remove(needDelete);
    }
}
