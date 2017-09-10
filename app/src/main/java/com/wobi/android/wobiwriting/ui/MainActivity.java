package com.wobi.android.wobiwriting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.views.TargetToolBar;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final String HOME_FRAG_TAG ="home_frag_tag";
    private static final String MOMENTS_FRAG_TAG ="moments_frag_tag";
    private static final String ME_FRAG_TAG ="me_frag_tag";
    private TargetToolBar mHomeBar;
    private TargetToolBar mMomentsBar;
    private TargetToolBar mMeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        performClickHome();

    }

    private void performClickHome(){
        mHomeBar.performClick();
    }

    private void initViews(){
        mHomeBar = (TargetToolBar)findViewById(R.id.home);
        mMomentsBar = (TargetToolBar)findViewById(R.id.moments);
        mMeBar = (TargetToolBar)findViewById(R.id.me);
        mHomeBar.setOnClickListener(this);
        mMomentsBar.setOnClickListener(this);
        mMeBar.setOnClickListener(this);
    }

    private void showFragment(String tag){
        FragmentManager fm = this.getSupportFragmentManager();

        Fragment existFrag = (Fragment)fm.findFragmentByTag(tag);
        if (existFrag == null || !existFrag.getTag().equals(tag)) {
            if (tag.equals(HOME_FRAG_TAG)) {
                existFrag = new HomeFragment();
            } else if (tag.equals(MOMENTS_FRAG_TAG)) {
                existFrag = new MomentsFragment();
            } else if (tag.equals(ME_FRAG_TAG)) {
                existFrag = new MeFragment();
            }
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, existFrag, tag);
            dealFragment(tag, existFrag, ft);
        }else {
            FragmentTransaction ft = fm.beginTransaction();
            dealFragment(tag, existFrag, ft);
        }
    }

    private void dealFragment(String tag, Fragment existFrag, FragmentTransaction ft){
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment home = (Fragment)fm.findFragmentByTag(HOME_FRAG_TAG);
        Fragment moments = (Fragment)fm.findFragmentByTag(MOMENTS_FRAG_TAG);
        Fragment me = (Fragment)fm.findFragmentByTag(ME_FRAG_TAG);
        if (existFrag.isDetached()){
            ft.attach(existFrag);
        }
        ft.show(existFrag);
        if (tag.equals(HOME_FRAG_TAG)){
            if (moments != null){
                ft.hide(moments);
            }
            if (me != null){
                ft.hide(me);
            }
        }else if (tag.equals(MOMENTS_FRAG_TAG)){
            if (home != null){
                ft.hide(home);
            }
            if (me != null){
                ft.hide(me);
            }
        }else if (tag.equals(ME_FRAG_TAG)){
            if (home != null){
                ft.hide(home);
            }
            if (moments != null){
                ft.hide(moments);
            }
        }
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                showFragment(HOME_FRAG_TAG);
                mHomeBar.setSelected();
                mMeBar.setNoSelected();
                mMomentsBar.setNoSelected();
                break;
            case R.id.moments:
                showFragment(MOMENTS_FRAG_TAG);
                mMomentsBar.setSelected();
                mMeBar.setNoSelected();
                mHomeBar.setNoSelected();
                break;
            case R.id.me:
                showFragment(ME_FRAG_TAG);
                mMeBar.setSelected();
                mHomeBar.setNoSelected();
                mMomentsBar.setNoSelected();
                break;
        }
    }
}
