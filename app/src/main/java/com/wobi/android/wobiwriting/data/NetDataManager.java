package com.wobi.android.wobiwriting.data;

/**
 * Created by wangyingren on 2017/9/12.
 */

public class NetDataManager {

    private static class NetDataManagerHolder{
        private static final NetDataManager instance = new NetDataManager();
    }

    public static NetDataManager getInstance(){
        return NetDataManagerHolder.instance;
    }

    private NetDataManager(){
        createMessageSender();
        createNetClient();
        createResponseDispatcher();
    }

    private void createMessageSender(){

    }

    private void createNetClient(){

    }

    private void createResponseDispatcher(){

    }
}
