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

    private static final String APP_UPGRADE_VERSION ="app_upgrade_version";

    private static final String FIVE_YEARS_VERSION = "five_years_version";
    private static final String XX_JC_ID_DISPLAY = "xx_jc_id_display";
    private static final String ZX_JC_ID_DISPLAY = "zx_jc_id_display";
    private static final String GRADE_ID= "grade_id";
    private static final String TERM_NUMBER= "term_number";

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

    public static void setShowedAppGradeVersion(Context context, int versionCode){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(APP_UPGRADE_VERSION,versionCode);
        editor.commit();
    }

    public static int getShowedAppGradeVersion(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(APP_UPGRADE_VERSION, -1);
    }

    public static void updateFiveYearsVersion(Context context, boolean enable){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean(FIVE_YEARS_VERSION,enable);
        editor.commit();
    }

    public static boolean getFiveYearsVersion(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getBoolean(FIVE_YEARS_VERSION, false);
    }

    public static void setXX_JC_ID(Context context, int jc_id){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(XX_JC_ID_DISPLAY,jc_id);
        editor.commit();
    }

    public static void setZX_JC_ID(Context context, int jc_id){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(ZX_JC_ID_DISPLAY,jc_id);
        editor.commit();
    }

    public static int getXX_JC_ID(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(XX_JC_ID_DISPLAY, -1);
    }

    public static int getZX_JC_ID(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(ZX_JC_ID_DISPLAY, -1);
    }

    public static void setGrade_ID(Context context, int grade_id){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(GRADE_ID,grade_id);
        editor.commit();
    }

    public static int getGrade_ID(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(GRADE_ID, -1);
    }

    public static void setTerm_num(Context context, int term_num){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt(TERM_NUMBER,term_num);
        editor.commit();
    }

    public static int getTerm_num(Context context){
        SharedPreferences userSettings = context.
                getSharedPreferences(WOBI_APP_INFO, Activity.MODE_PRIVATE);
        return userSettings.getInt(TERM_NUMBER, -1);
    }
}
