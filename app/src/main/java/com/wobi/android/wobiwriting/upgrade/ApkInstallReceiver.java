package com.wobi.android.wobiwriting.upgrade;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wangyingren on 2018/1/13.
 */

public class ApkInstallReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long downloadApkId =intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context,long downloadApkId) {
        AppUpgradeManager.getInstance(context).install(downloadApkId);
    }
}
