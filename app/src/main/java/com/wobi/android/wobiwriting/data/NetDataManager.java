package com.wobi.android.wobiwriting.data;

import android.util.Log;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.data.dispatcher.ResponseDispatcher;
import com.wobi.android.wobiwriting.data.message.ConnManagerRequest;
import com.wobi.android.wobiwriting.data.message.ConnManagerResponse;
import com.wobi.android.wobiwriting.data.sender.IMessageSender;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wangyingren on 2017/9/12.
 */

public class NetDataManager {

    private Gson gson = new Gson();
    //http post data sender
    private IMessageSender messageSender;
    // 创建会话Id
    private final DialogRequestIdHandler dialogRequestIdHandler;
    private ResponseDispatcher dispatcher;
    private NetworkClient networkClient;

    private static class NetDataManagerHolder{
        private static final NetDataManager instance = new NetDataManager();
    }

    public static NetDataManager getInstance(){
        return NetDataManagerHolder.instance;
    }

    private NetDataManager(){
        dialogRequestIdHandler = new DialogRequestIdHandler();
        createMessageSender();
        createResponseDispatcher();
        createNetClient();
    }

    public IMessageSender getMessageSender(){
        return messageSender;
    }

    private void createMessageSender(){

        messageSender = new IMessageSender(){

            @Override
            public void sendEvent(String event, IResponseListener listener) {
                networkClient.sendRequest(event,listener);
            }
        };
    }

    private void createNetClient(){
        networkClient = new NetworkClient(dispatcher);
    }

    private void createResponseDispatcher(){
        dispatcher = new ResponseDispatcher();
    }

    public void getBusinessServerAddress(IResponseListener listener){
        ConnManagerRequest request = new ConnManagerRequest();
        String json = request.jsonToString(request);
        networkClient.getBusinessServerAddress(json, listener);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.e("yingrenw","code  == "+response.code());
        return response.body().string();
    }
}
