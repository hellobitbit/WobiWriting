package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.Grade;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/22.
 */

public class GetGradeResponse extends Response {


    private List<Grade> grade_list;

    public List<Grade> getGradeList(){
        return grade_list;
    }
}
