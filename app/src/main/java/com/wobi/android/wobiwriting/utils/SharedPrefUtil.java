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

}
