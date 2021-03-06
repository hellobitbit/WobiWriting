package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.WobiWritingApplication;
import com.wobi.android.wobiwriting.me.MyInformationActivity;
import com.wobi.android.wobiwriting.me.MyJcSelectionActivty;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.TargetToolBar;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    public static final String FRAGMENT_DISPLAY_POSITION = "fragment_display_position";
    private static final String TAG = "MainActivity";
    private static final String HOME_FRAG_TAG ="home_frag_tag";
    private static final String MOMENTS_FRAG_TAG ="moments_frag_tag";
    private static final String ME_FRAG_TAG ="me_frag_tag";
    private TargetToolBar mHomeBar;
    private TargetToolBar mMomentsBar;
    private TargetToolBar mMeBar;

    private String prevTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WobiWritingApplication.getInstance().registerActivity(this);
//        initStatusBar();
        initViews();
        displayFrag(getIntent());

        if (SharedPrefUtil.getXX_JC_ID(getApplicationContext()) == -1
                || SharedPrefUtil.getZX_JC_ID(getApplicationContext()) == -1){
            Intent jcSelection = new Intent(MainActivity.this, MyJcSelectionActivty.class);
            startActivity(jcSelection);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        int position = intent.getIntExtra(FRAGMENT_DISPLAY_POSITION, -1);

        if (position == 2){
            displayFrag(intent);
        }else if(position == -1){
            FragmentManager fm = this.getSupportFragmentManager();
            try {
                MeFragment me = (MeFragment) fm.findFragmentByTag(ME_FRAG_TAG);
                if (me != null){
                    me.doWeiboResultIntent(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void displayFrag(Intent intent){
        int position = intent.getIntExtra(FRAGMENT_DISPLAY_POSITION, 0);
        if (position == 0){
            performClickHome();
        }else if (position == 1){
            performClickMoments();
        }else if (position == 2){
            performClickMe();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        WobiWritingApplication.getInstance().unRegisterActivity(this);
    }

    private void performClickHome(){
        mHomeBar.performClick();
    }

    private void performClickMoments(){
        mMomentsBar.performClick();
    }

    private void performClickMe(){
        mMeBar.performClick();
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
                updateStatusDisplay(R.color.gray);
                prevTag = HOME_FRAG_TAG;
                break;
            case R.id.moments:
                showFragment(MOMENTS_FRAG_TAG);
                mMomentsBar.setSelected();
                mMeBar.setNoSelected();
                mHomeBar.setNoSelected();
                updateStatusDisplay(R.color.colorPrimaryDark);
                prevTag = MOMENTS_FRAG_TAG;
                break;
            case R.id.me:
                showFragment(ME_FRAG_TAG);
                mMeBar.setSelected();
                mHomeBar.setNoSelected();
                mMomentsBar.setNoSelected();
                updateStatusDisplay(R.color.colorPrimaryDark);
                updateMeFragmentState();
                break;
        }
    }

    private void updateStatusDisplay(int resId){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(resId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == LoginActivity.REQUEST_CODE
                && resultCode == LoginActivity.RESULT_CODE_SUCCESS){
            updateMeFragmentState();
        }else if (requestCode == MyInformationActivity.REQUEST_CODE){
            updateMeFragmentState();
        }


        FragmentManager fm = this.getSupportFragmentManager();
        try {
            MeFragment me = (MeFragment) fm.findFragmentByTag(ME_FRAG_TAG);
            if (me != null){
                me.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateMeFragmentState(){
        FragmentManager fm = this.getSupportFragmentManager();
        try {
            MeFragment me = (MeFragment) fm.findFragmentByTag(ME_FRAG_TAG);
            if (me != null){
                me.refreshLoginState();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void switchToPrevFragmentWhenCancelLoginTips(){
        if (HOME_FRAG_TAG.equals(prevTag)){
            mHomeBar.performClick();
        }else if(MOMENTS_FRAG_TAG.equals(prevTag)){
            mMomentsBar.performClick();
        }
    }

    void startLoginActivity(int request_code){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent,request_code);
    }

    void startUserInfoActivity(Intent intent, int request_code){
        startActivityForResult(intent,request_code);
    }

    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
    public void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(this);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色
            rectView.setBackgroundColor(getResources().getColor(R.color.gray));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }
}
