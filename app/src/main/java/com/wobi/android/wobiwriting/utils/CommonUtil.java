package com.wobi.android.wobiwriting.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wangyingren on 2017/9/24.
 */

public class CommonUtil {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINESE);
        return format.format(new Date());
    }
}
