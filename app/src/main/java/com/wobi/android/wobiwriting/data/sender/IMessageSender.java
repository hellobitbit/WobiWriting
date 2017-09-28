package com.wobi.android.wobiwriting.data.sender;

import com.wobi.android.wobiwriting.data.IResponseListener;

/**
 * Created by wangyingren on 2017/9/24.
 */

public interface IMessageSender {

    public void sendEvent(String event, IResponseListener responseListener);
}
