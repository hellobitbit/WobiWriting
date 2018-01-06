package com.wobi.android.wobiwriting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wangyingren on 2017/9/27.
 */

public class SharedPrefUtil {

    private static final String WOBI_APP_INFO = "wobi-app";
    private static final String BUSINESS_URL = "business-url";
    private static final String USER_INFO = "user_info";
    private static final String SESSION_ID = "session_id";
    private static final String USER_PASSWORD = "user_password";
    private static final String GUIDE_USED = "guide_used";
    private static final String LAST_LOGIN_ACCOUNT = "last_login_account";

    private static final String KEWEN_DIRECTORY_POSITION = "kewen_directory_position";
    private static final String SZ_POSITION = "sz_position";

    private static final String HOME_GRADE_TIPS_DISPLAY = "home_grade_tips_display";
    private static final String HOME_REGISTER_TIPS_DISPLAY = "home_register_tips_display";
    private static final String MOMENT_TIPS_DISPLAY = "moment_tips_display";
    private static final String ME_TIPS_DISPLAY = "me_tips_display";

    private static final String COMMUNITY_INFOS_FOR_PURCHASE = "community_infos_for_chase";

    public static void saveBusinessUrl(Context context, String url){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString(BUSINESS_URL,url);
        editor.commit();
    }

    public static String getBusinessUrl(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getString(BUSINESS_URL,"");
    }

    public static void saveLoginInfo(Context context, String userInfo){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString(USER_INFO,userInfo);
        editor.commit();
    }

    public static String getLoginInfo(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getString(USER_INFO,"");
    }

    public static void saveLastLoginAccount(Context context, String account){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString(LAST_LOGIN_ACCOUNT,account);
        editor.commit();
    }

    public static String getLastLoginAccount(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getString(LAST_LOGIN_ACCOUNT,"");
    }

    public static void saveLoginPassword(Context context, String password){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString(USER_PASSWORD,password);
        editor.commit();
    }

    public static String getLoginPassword(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getString(USER_PASSWORD,"");
    }

    public static void saveSessionId(Context context, String sessionId){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString(SESSION_ID,sessionId);
        editor.commit();
    }

    public static String getSessionId(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getString(SESSION_ID,"");
    }

    public static void saveGuideState(Context context, boolean used){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean(GUIDE_USED,used);
        editor.commit();
    }

    public static boolean getGuideState(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getBoolean(GUIDE_USED,false);
    }

    public static void saveKewenDirectoryPosition(Context context, int position){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(KEWEN_DIRECTORY_POSITION,position);
        editor.commit();
    }

    public static int getKewenDirectoryPosition(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(KEWEN_DIRECTORY_POSITION, 0);
    }

    public static void saveSZPosition(Context context, int position){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(SZ_POSITION,position);
        editor.commit();
    }

    public static int getSZPosition(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(SZ_POSITION, 0);
    }

//    public static void saveHomeGradeTipsState(Context context, boolean hasDisplayed){
//        SharedPreferences userSettings = context.
//                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userSettings.edit();
//        editor.putBoolean(HOME_GRADE_TIPS_DISPLAY,hasDisplayed);
//        editor.commit();
//    }
//
//    public static boolean getHomeGradeTipsState(Context context){
//        SharedPreferences userSettings = context.
//                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
//        return userSettings.getBoolean(HOME_GRADE_TIPS_DISPLAY, false);
//    }

    public static void saveHomeRegisterTipsState(Context context, boolean hasDisplayed){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean(HOME_REGISTER_TIPS_DISPLAY,hasDisplayed);
        editor.commit();
    }

    public static boolean getHomeRegisterTipsState(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getBoolean(HOME_REGISTER_TIPS_DISPLAY, false);
    }

    public static void saveMomentTipsState(Context context, boolean hasDisplayed){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean(MOMENT_TIPS_DISPLAY,hasDisplayed);
        editor.commit();
    }

    public static boolean getMomentTipsState(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getBoolean(MOMENT_TIPS_DISPLAY, false);
    }

    public static void saveMeTipsState(Context context, boolean hasDisplayed){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean(ME_TIPS_DISPLAY,hasDisplayed);
        editor.commit();
    }

    public static boolean getMeTipsState(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getBoolean(ME_TIPS_DISPLAY, false);
    }

//    public static void saveCommunityInfosForPurchase(Context context, String communityInfos){
//        SharedPreferences userSettings = context.
//                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userSettings.edit();
//        editor.putString(COMMUNITY_INFOS_FOR_PURCHASE,communityInfos);
//        editor.commit();
//    }
//
//    public static String getCommunityInfosForPurchase(Context context){
//        SharedPreferences userSettings = context.
//                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
//        return userSettings.getString(COMMUNITY_INFOS_FOR_PURCHASE, "");
//    }
}
