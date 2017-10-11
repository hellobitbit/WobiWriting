package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.CNClassicCourse;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/8.
 */

public class DiZiGuiResponse extends Response {
    private List<CNClassicCourse> dzj_list;

    public List<CNClassicCourse> getDzjList(){
        return dzj_list;
    }
}
