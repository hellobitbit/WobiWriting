package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.BusinessType;
import com.wobi.android.wobiwriting.data.message.Request;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetKWMLListRequest extends Request {

    private int jc_id; // 教材版本id

    private String grade_id;

    public GetKWMLListRequest(){
        setRequestType(BusinessType.BT_Get_KWML_List.getValue());
        mInstance = this;
    }

    public void setJcId(int jc_id){
        this.jc_id = jc_id;
    }

    public void setGradeId(String grade_id){
        this.grade_id = grade_id;
    }
}
