package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.CalligraphyClassCourse;
import com.wobi.android.wobiwriting.home.model.Course;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class ShuFaKeTangResponse extends Response {

    private  List<CalligraphyClassCourse> sfkt_list;

    public List<CalligraphyClassCourse> getSfktList(){
        return sfkt_list;
    }
}
