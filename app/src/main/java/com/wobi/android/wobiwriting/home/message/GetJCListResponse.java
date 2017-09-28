package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetJCListResponse extends Response {

    private List<JiaoCaiObject> jc_list;

    public List<JiaoCaiObject> getJcList(){
        return jc_list;
    }
}
