package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class ShuFaKeTangRequest extends Request {

    private int grade_id; // 年级

    private int term_num; // 学期

    public void setGradeId(int grade_id){
        this.grade_id = grade_id;
    }

    public void setTermNum(int term_num){
        this.term_num = term_num;
    }
}
