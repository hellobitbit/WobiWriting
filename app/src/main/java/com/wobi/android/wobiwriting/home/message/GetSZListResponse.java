package com.wobi.android.wobiwriting.home.message;

import com.wobi.android.wobiwriting.data.message.Response;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetSZListResponse extends Response {

    private List<String> sz_list;

    public List<String> getSzList(){
        return sz_list;
    }
}
