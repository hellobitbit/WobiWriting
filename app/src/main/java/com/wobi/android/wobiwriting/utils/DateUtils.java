package com.wobi.android.wobiwriting.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangyingren on 2017/10/23.
 */

public class DateUtils {

    public static String getCurrentTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String parseDateString(String str){
        Date date = null;
        try {
            date = stringToDate(str,"yyyy-MM-dd HH:mm:ss");
            String result = dateToString(date,"MM-dd HH:mm");
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseDateToVipData(String str){
        Date date = null;
        try {
            date = stringToDate(str,"yyyy-MM-dd HH:mm:ss");
            String result = dateToString(date,"yyyy-MM-dd");
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
}
