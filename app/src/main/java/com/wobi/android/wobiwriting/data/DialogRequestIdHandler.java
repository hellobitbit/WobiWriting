package com.wobi.android.wobiwriting.data;

import java.util.UUID;

/**
 * Created by wangyingren on 2017/9/24.
 */

public class DialogRequestIdHandler {
    private String activeDialogRequestId;

    public DialogRequestIdHandler() {
    }

    public String createActiveDialogRequestId() {
        activeDialogRequestId = UUID.randomUUID().toString();
        return activeDialogRequestId;
    }

    /**
     * 判断当前dialogRequestId是否活跃的
     *
     * @param dialogRequestId dialogRequestId
     * @return dialogRequestId与activeDialogRequestId相等则返回true，否则返回false
     */
    public Boolean isActiveDialogRequestId(String dialogRequestId) {
        return activeDialogRequestId != null && activeDialogRequestId.equals(dialogRequestId);
    }
}
