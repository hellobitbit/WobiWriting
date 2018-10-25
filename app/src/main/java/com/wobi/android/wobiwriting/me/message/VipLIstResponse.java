package com.wobi.android.wobiwriting.me.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.me.model.VipPackage;

import java.util.List;

/**
 * Created by wangyingren on 2018/10/22.
 */

public class VipLIstResponse extends Response {

    List<VipPackage> vip_package_list;

    public List<VipPackage> getVip_package_list(){
        return vip_package_list;
    }
}
