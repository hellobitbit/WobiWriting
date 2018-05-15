package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.CNClassicCourse;

import java.util.List;

/**
 * Created by wangyingren on 2018/4/21.
 */

public class GushiResponse extends Response {
    private List<CNClassicCourse> gssd_list;

    public List<CNClassicCourse> getGssd_list(){
        return gssd_list;
    }
}
