package com.wobi.android.wobiwriting.http.exceptions;

/**
 * Created by wangyingren on 2017/9/24.
 */
public class Exceptions {
    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}