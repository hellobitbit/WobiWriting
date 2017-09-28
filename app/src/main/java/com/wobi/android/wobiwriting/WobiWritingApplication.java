package com.wobi.android.wobiwriting;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.ConnManagerRequest;
import com.wobi.android.wobiwriting.data.message.ConnManagerResponse;
import com.wobi.android.wobiwriting.http.HttpConfig;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class WobiWritingApplication extends Application{
    private NetDataManager netDataManager;
    private Gson gson = new Gson();

    @Override
    public void onCreate(){
        super.onCreate();
        initNetworkManager();
        initImageLoader(getApplicationContext());
    }


    private void initImageLoader(Context context){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
    }

    private void initNetworkManager(){
        netDataManager = NetDataManager.getInstance();
        netDataManager.getBusinessServerAddress(new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                ConnManagerResponse obj = gson.fromJson(response, ConnManagerResponse.class);
                SharedPrefUtil.saveBusinessUrl(WobiWritingApplication.this, obj.getUrl());
                HttpConfig.setBusinessServer(obj.getUrl());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
    }

}
