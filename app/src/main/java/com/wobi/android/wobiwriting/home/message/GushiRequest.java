package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2018/4/21.
 */

public class GushiRequest extends Request {

    private int jc_id;
    private int grade_id;
    private int term_num;

    public GushiRequest(){
        setRequestType(BusinessType.BT_Gu_Shi.getValue());
        mInstance = this;
    }

    public void setJc_id(int jc_id){
        this.jc_id = jc_id;
    }

    public void setGrade_id(int grade_id){
        this.grade_id = grade_id;
    }

    public void setTerm_num(int term_num){
        this.term_num = term_num;
    }
}
