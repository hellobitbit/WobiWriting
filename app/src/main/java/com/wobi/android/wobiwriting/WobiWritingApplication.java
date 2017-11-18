package com.wobi.android.wobiwriting;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.data.message.ConnManagerResponse;
import com.wobi.android.wobiwriting.http.HttpConfig;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class WobiWritingApplication extends Application{
    private static final String TAG = "WobiWritingApplication";
    private NetDataManager netDataManager;
    private Gson gson = new Gson();

    private Handler handler = new Handler();

    private List<Activity> mActivities = new ArrayList<>();
    private static WobiWritingApplication sInstance;

    public static synchronized WobiWritingApplication getInstance(){
        return sInstance;
    }

    public void registerActivity(Activity activity){
        if (!mActivities.contains(activity)){
            mActivities.add(activity);
        }
    }

    public void unRegisterActivity(Activity activity){
        mActivities.remove(activity);
    }

    public Activity getTopActivity(){
        return mActivities.size() > 0 ? mActivities.get(mActivities.size()-1) : null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
        HttpConfig.setSessionId(SharedPrefUtil.getSessionId(getApplicationContext()));
        useCachedBusinessServer();
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
                LogUtil.d(TAG," response: "+response);
                ConnManagerResponse obj = gson.fromJson(response, ConnManagerResponse.class);
                SharedPrefUtil.saveBusinessUrl(WobiWritingApplication.this, obj.getUrl());
                HttpConfig.setBusinessServer(obj.getUrl());
            }

            @Override
            public void onFailed(String errorMessage) {

            }
        });
    }

    private void useCachedBusinessServer(){
        HttpConfig.setBusinessServer(SharedPrefUtil.getBusinessUrl(WobiWritingApplication.this));
    }

    private void displaySplash(){

        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 更多type：https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_PHONE
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        wmParams.format = PixelFormat.TRANSLUCENT;
        // 更多falgs:https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_NOT_FOCUSABLE
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.app_overlay_layout, null);
        wm.addView(view, wmParams);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wm.removeView(view);
            }
        }, 3000);
    }
}
