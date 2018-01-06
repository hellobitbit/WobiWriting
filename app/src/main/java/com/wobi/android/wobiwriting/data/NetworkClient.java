package com.wobi.android.wobiwriting.data;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.wobi.android.wobiwriting.WobiWritingApplication;
import com.wobi.android.wobiwriting.data.dispatcher.ResponseDispatcher;
import com.wobi.android.wobiwriting.data.heartbeat.HeartBeat;
import com.wobi.android.wobiwriting.http.HttpConfig;
import com.wobi.android.wobiwriting.http.HttpRequestInterface;
import com.wobi.android.wobiwriting.http.OkHttpRequestImpl;
import com.wobi.android.wobiwriting.http.callback.ResponseCallback;
import com.wobi.android.wobiwriting.http.callback.HttpCallback;
import com.wobi.android.wobiwriting.ui.MainActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/24.
 */

public class NetworkClient {

    private Handler mSenderHandler = new Handler(Looper.getMainLooper());
    private static final String TAG = "NetworkClient";
    private final HttpRequestInterface httpRequestImp;
    private final HeartBeat heartBeat;
    private final ResponseDispatcher responseDispatcher;

    public NetworkClient(ResponseDispatcher responseDispatcher) {
        this.responseDispatcher = responseDispatcher;
        httpRequestImp = new OkHttpRequestImpl();
        heartBeat = new HeartBeat(httpRequestImp);
        heartBeat.setHeartbeatListener(new HeartBeat.IHeartbeatListener() {
            @Override
            public void onStartConnect() {
//                startConnect();
            }
        });
    }

    public void release() {
        heartBeat.release();
        httpRequestImp.cancelRequest(HttpConfig.HTTP_EVENT_TAG);
    }

    /**
     * 发送普通请求
     *
     * @param requestBody 消息体
     * @param listener    回调
     */
    public void sendRequest(String requestBody, IResponseListener listener) {
        httpRequestImp.doPostEventStringAsync(requestBody,
                getResponseCallback(responseDispatcher, listener));
    }

    public void getBusinessServerAddress(String requestBody, IResponseListener listener) {
        httpRequestImp.cancelRequest(HttpConfig.HTTP_MANAGER_TAG);
        httpRequestImp.getBusinessServerAsync(requestBody,
                getResponseCallback(responseDispatcher, listener));
    }

    private HttpCallback getResponseCallback(ResponseDispatcher responseDispatcher,
                                             final IResponseListener listener) {
        ResponseCallback responseCallback = new ResponseCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.d(TAG, "onError,", e);
                if (listener != null) {
                    listener.onFailed("网络异常");
                }
            }

            @Override
            public void onResponse(Response response, int id) {
                super.onResponse(response, id);
                // do nothing
            }

            @Override
            public Response parseNetworkResponse(Response response, int id) throws Exception {
                final int statusCode = response.code();
                final String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);

                if (statusCode == 200) {
                    final String handle_result = jsonObject.optString("handle_result");
                    final String request_result = jsonObject.optString("request_result");
                    LogUtil.d(TAG," handle_result = "+handle_result +" request_result = "+request_result);
                    if (TextUtils.isEmpty(handle_result) || !handle_result.equals("OK")
                            || TextUtils.isEmpty(request_result) || !request_result.equals("OK")) {
                        if (!TextUtils.isEmpty(handle_result) && handle_result.equals("wrong session id")) {
                            if (WobiWritingApplication.getInstance().getTopActivity() != null) {
                                mSenderHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        displayReLoginWindow();
                                    }
                                });
                            }
                        }else {
                            if (listener != null) {
                                mSenderHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!TextUtils.isEmpty(handle_result)
                                                && !handle_result.equals("OK")) {
                                            listener.onFailed(handle_result);
                                        } else if (!TextUtils.isEmpty(request_result)
                                                && !request_result.equals("OK")) {
                                            listener.onFailed(request_result);
                                        } else {
                                            listener.onFailed("网络异常");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        mSenderHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSucceed(json);
                            }
                        });
                    }
                }
                return response;
            }
        };

        return responseCallback;
    }

    private void displayReLoginWindow(){
        Activity activity = WobiWritingApplication.getInstance().getTopActivity();
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setMessage("账号长时间未登录或者别处登录");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("下线通知");
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearLoginInfo();
                Intent intent = new Intent(WobiWritingApplication.getInstance(), MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_DISPLAY_POSITION, 2);
                WobiWritingApplication.getInstance().startActivity(intent);

            }
        });

        builder.setNegativeButton("退出",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearLoginInfo();
                        WobiWritingApplication.getInstance().clearAllScreens();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void clearLoginInfo(){
        SharedPrefUtil.saveLoginInfo(WobiWritingApplication.getInstance(), "");
        SharedPrefUtil.saveSessionId(WobiWritingApplication.getInstance(), "");
        SharedPrefUtil.saveLoginPassword(WobiWritingApplication.getInstance(), "");
        SharedPrefUtil.saveKewenDirectoryPosition(WobiWritingApplication.getInstance(), 0);
        SharedPrefUtil.saveSZPosition(WobiWritingApplication.getInstance(), 0);
        HttpConfig.setSessionId(SharedPrefUtil.getSessionId(WobiWritingApplication.getInstance()));
    }

    public interface ILoginListener {
        void onConnected();

        void onUnconnected();
    }
}
