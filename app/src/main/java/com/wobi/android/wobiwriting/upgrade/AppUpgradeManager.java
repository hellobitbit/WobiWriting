package com.wobi.android.wobiwriting.upgrade;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.upgrade.message.CheckUpdateRequest;
import com.wobi.android.wobiwriting.upgrade.message.CheckUpdateResponse;
import com.wobi.android.wobiwriting.upgrade.model.LatestAppVersion;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by wangyingren on 2018/1/13.
 */

public class AppUpgradeManager {

    private final DownloadManager downManager;
    private Gson gson = new Gson();
    private Handler handler = new Handler(Looper.getMainLooper());
    private static AppUpgradeManager sInstance;
    private final Context mContext;
    private static final String TAG = "AppUpgradeManager";
    private ProgressDialog pd;

    public  static AppUpgradeManager getInstance(Context context){
        if (sInstance == null){
            sInstance = new AppUpgradeManager(context);
        }
        return sInstance;
    }

    private AppUpgradeManager(Context context){
        mContext = context;
        downManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void checkUpdate(final boolean auto) {
        if (!auto){
            showDialog("正在获取版本信息");
        }
        CheckUpdateRequest request = new CheckUpdateRequest();
        request.setPlatform(1);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG, " response: " + response);
                CheckUpdateResponse checkUpdateResponse = gson.fromJson(response,
                        CheckUpdateResponse.class);
                if (checkUpdateResponse != null && checkUpdateResponse.getLatest_app_version() != null){
                    LatestAppVersion latestAppVersion = checkUpdateResponse.getLatest_app_version();
                    int versionCode = getVersionCode(mContext);
                    if (versionCode != -1
                            && latestAppVersion.getVer_num() > versionCode){
                        LogUtil.d(TAG,"need update");

                        if (latestAppVersion.getVer_num()!=
                                SharedPrefUtil.getShowedAppGradeVersion(mContext) || !auto){
                            getApkSize(latestAppVersion.getSoft_url(), latestAppVersion.getVer_name(),
                                    latestAppVersion.getVer_num());
                        }

                    }else {
                        LogUtil.d(TAG,"not need update");
                        if (!auto){
                            dismissDialog();
                            showErrorMsg("当前版本是最新版本");
                        }
                    }
                }else {
                    dismissDialog();
                    showErrorMsg("获取版本信息失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG, " error: " + errorMessage);
                dismissDialog();
                showErrorMsg(errorMessage);
            }
        });
    }

    private void getApkSize(final String url,final String version_name, final int version_code){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL myURL = new URL(url);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                    //处理下载读取长度为-1 问题
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.connect();
                    // 获取文件大小
                    long length = conn.getContentLength();
                    LogUtil.d(TAG,"getApkSize = "+length);
                    DecimalFormat df = new DecimalFormat("######0.00");
                    double size = (double) length/(1024 * 1024);
                    final String result = df.format(size);
                    conn.disconnect();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            displayPopupWindowTips(version_name,result, url, version_code);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                    dismissDialog();
                    showErrorMsg("获取版本信息失败");
                }

            }
        }).start();
    }

    private void displayPopupWindowTips(String version_name, String size, final String url, int version_code) {
        View layout = ((Activity)mContext).getLayoutInflater().inflate(R.layout.app_upgrade_overlay_layout, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        TextView versionName = (TextView)layout.findViewById(R.id.version_name);
        versionName.setText("最新版本 : "+version_name);
        TextView versionSize = (TextView)layout.findViewById(R.id.version_size);
        versionSize.setText("新版本大小 : "+ size+"M");
        ImageView upgrade_now = (ImageView)layout.findViewById(R.id.upgrade_now);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        upgrade_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG,"displayPopupWindowTips upgrade_now");
                download(url);
                pop.dismiss();
            }
        });
//        layout.setBackgroundResource(imageResId);
        pop.setClippingEnabled(false);
        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.showAtLocation(((Activity)mContext).findViewById(R.id.container), Gravity.CENTER, 0, 0);
        SharedPrefUtil.setShowedAppGradeVersion(mContext, version_code);
        dismissDialog();
    }

    private int getVersionCode(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            int versionCode = packInfo.versionCode;
            LogUtil.d(TAG," getVersionCode versionCode = "+versionCode);
            return versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    private void download(String apkurl){
        showErrorMsg("开始下载");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkurl));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("download", "wobi_update.apk");
        request.setDescription("软件新版本下载");
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();

        long id= downManager.enqueue(request);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID,id).commit();
    }

    public void install(long downloadApkId){
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        long downId =sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if(downloadApkId == downId){
            Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            String filePath = getRealFilePath(mContext, downloadFileUri);
            LogUtil.d(TAG,"filePath = "+getRealFilePath(mContext, downloadFileUri) + " downloadApkId = "+downloadApkId);
            if (!TextUtils.isEmpty(filePath)) {
                Intent install= new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(new File(filePath)),
                        "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(install);
            }else{
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void showDialog(String tips){
        pd = new ProgressDialog(mContext);
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.setMessage(tips);
        pd.show();
    }

    private void dismissDialog(){
        if (pd != null){
            pd.dismiss();
        }
    }

    private void showErrorMsg(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
