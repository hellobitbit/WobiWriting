package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetKWMLListResponse extends Response {


    private List<KeWenDirectory> kwml_list; //课文目录列表

    public List<KeWenDirectory> getKwmlList(){
        return kwml_list;
    }
}
