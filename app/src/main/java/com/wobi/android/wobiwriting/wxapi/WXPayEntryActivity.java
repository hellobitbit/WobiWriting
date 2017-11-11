package com.wobi.android.wobiwriting.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.me.MyWodouActivity;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/11/10.
 */

public class WXPayEntryActivity extends ActionBarActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";
    private final String W_APPID = "wx811d8d46a1d5cb01";
    private IWXAPI api;
    private TextView share_weixin_result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_weixin_result_layout);
        setCustomActionBar();
        initViews();
        updateTitleText("微信支付结果");
        regToWx();
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        share_weixin_result = (TextView)findViewById(R.id.share_weixin_result);
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(WXPayEntryActivity.this, W_APPID, true);
        api.registerApp(W_APPID);
    }

    @Override
    public void onResp(BaseResp resp) { //在这个方法中处理微信传回的数据
        //形参resp 有下面两个个属性比较重要
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        switch (resp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                share_weixin_result.setText("支付完成");
                Intent intent = new Intent(WXPayEntryActivity.this, MyWodouActivity.class);
                intent.putExtra("支付完成", true);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                share_weixin_result.setText("用户取消");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                share_weixin_result.setText("一般错误");
                break;
            default:
                //其他不可名状的情况
                share_weixin_result.setText("其他不可名状的情况");
                break;
        }
        LogUtil.d(TAG,"resp.errCode == "+resp.errCode);
    }

    @Override
    public void onReq(BaseReq req) {
        //......这里是用来处理接收的请求,暂不做讨论
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return 0;
    }
}
