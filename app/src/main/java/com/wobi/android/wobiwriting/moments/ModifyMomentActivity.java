package com.wobi.android.wobiwriting.moments;

import android.os.Bundle;

/**
 * Created by wangyingren on 2017/11/1.
 */

public class ModifyMomentActivity extends NewOrModifyMomentBaseActivity{

    private static final String TAG = "NewMomentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitleText("圈子信息修改");
        updateRightText("保存");
    }

    @Override
    public void onClickActionBarTitle(){
        //new moment
//        modifyCommunity();
    }

    private void modifyCommunity(){

    }
}
