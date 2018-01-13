package com.wobi.android.wobiwriting.upgrade.message;

import com.wobi.android.wobiwriting.data.message.Response;
import com.wobi.android.wobiwriting.upgrade.model.LatestAppVersion;

/**
 * Created by wangyingren on 2018/1/13.
 */

public class CheckUpdateResponse extends Response {

    private LatestAppVersion latest_app_version;

    public LatestAppVersion getLatest_app_version(){
        return latest_app_version;
    }
}
