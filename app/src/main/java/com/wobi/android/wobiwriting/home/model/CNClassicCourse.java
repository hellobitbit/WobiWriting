package com.wobi.android.wobiwriting.home.model;

import com.wobi.android.wobiwriting.data.message.Response;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class CNClassicCourse extends Response {

    private String catalog_id;

    private String catalog_name;

    private int grade_id;

    private String jj_url;

    private String sd_url;

    private int term_num;

    private String course_name;

    private String course_num;

    private String gushi_url;

    private String id;

    private String jiedu_url;

    public String getCatalog_name(){
        return catalog_name;
    }

    public int getGrade_id(){
        return grade_id;
    }

    public int getTerm_num(){
        return term_num;
    }

    public String getJj_url(){
        return jj_url;
    }

    public String getSd_url(){
        return sd_url;
    }

    public String getCatalogId(){
        return catalog_id;
    }

    public String getCourseName(){
        return course_name;
    }

    public String getCourseNum(){
        return course_num;
    }

    public String getGushiUrl(){
        return gushi_url;
    }

    public String getId(){
        return id;
    }

    public String getJieduUrl(){
        return jiedu_url;
    }
}
