package com.wobi.android.wobiwriting.data;

import android.os.Handler;
import android.os.Looper;

import com.wobi.android.wobiwriting.data.dispatcher.ResponseDispatcher;
import com.wobi.android.wobiwriting.data.heartbeat.HeartBeat;
import com.wobi.android.wobiwriting.http.HttpConfig;
import com.wobi.android.wobiwriting.http.HttpRequestInterface;
import com.wobi.android.wobiwriting.http.OkHttpRequestImpl;
import com.wobi.android.wobiwriting.http.callback.ResponseCallback;
import com.wobi.android.wobiwriting.http.callback.HttpCallback;
import com.wobi.android.wobiwriting.utils.LogUtil;

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

    public NetworkClient(ResponseDispatcher responseDispatcher){
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

    public void getBusinessServerAddress(String requestBody, IResponseListener listener){
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
                    listener.onFailed(e.getMessage());
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
                if (statusCode == 200) {
                    mSenderHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSucceed(json);
                        }
                    });
                }
                return response;
            }
        };

        return responseCallback;
    }

    public interface ILoginListener {
        void onConnected();

        void onUnconnected();
    }
}
