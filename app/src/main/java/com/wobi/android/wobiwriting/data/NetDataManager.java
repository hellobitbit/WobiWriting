package com.wobi.android.wobiwriting.data;

import com.wobi.android.wobiwriting.data.dispatcher.ResponseDispatcher;
import com.wobi.android.wobiwriting.data.message.ConnManagerRequest;
import com.wobi.android.wobiwriting.data.sender.IMessageSender;

/**
 * Created by wangyingren on 2017/9/12.
 */

public class NetDataManager {
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
        String json = request.jsonToString();
        networkClient.getBusinessServerAddress(json, listener);
    }
}
