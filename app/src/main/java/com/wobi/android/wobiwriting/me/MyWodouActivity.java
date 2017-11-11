package com.wobi.android.wobiwriting.me;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.me.message.BuyVIPServiceRequest;
import com.wobi.android.wobiwriting.me.message.BuyVIPServiceResponse;
import com.wobi.android.wobiwriting.me.message.GetWXPayResultRequest;
import com.wobi.android.wobiwriting.me.message.GetWXPayResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchJoinedCommunityRequest;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import org.json.JSONException;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyWodouActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MyWodouActivity";
    private EditText request_code_edit;
    private UserGetInfoResponse userInfo;
    private BuyVIPServiceResponse buyVIPServiceResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wodou_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        initViews();
        setCustomActionBar();

    }

    private void initViews(){

        TextView tao_can1_purchase = (TextView)findViewById(R.id.tao_can1_purchase);
        TextView tao_can2_purchase = (TextView)findViewById(R.id.tao_can2_purchase);
        TextView tao_can3_purchase = (TextView)findViewById(R.id.tao_can3_purchase);
        TextView tao_can4_purchase = (TextView)findViewById(R.id.tao_can4_purchase);

        tao_can1_purchase.setOnClickListener(this);
        tao_can2_purchase.setOnClickListener(this);
        tao_can3_purchase.setOnClickListener(this);
        tao_can4_purchase.setOnClickListener(this);

        request_code_edit = (EditText)findViewById(R.id.request_code_edit);
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_my_wodou_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tao_can1_purchase:
                purchase("学期卡（6个月）",6, 60, 49.8);
                break;
            case R.id.tao_can2_purchase:
                purchase("学年卡（12个月）",12, 120, 90);
                break;
            case R.id.tao_can3_purchase:
                purchase("两年卡（24个月）",24, 240, 170);
                break;
            case R.id.tao_can4_purchase:
                purchase("三年卡（36个月）",36, 360, 240);
                break;
        }
    }

    private void purchase(String title, int period, double originPrice, double price){
        if (!TextUtils.isEmpty(request_code_edit.getText().toString())){
            hideSoftware();
            showPopupWindow(title, period, originPrice, price);
        }else {
            showErrorMsg("邀请码不能为空");
        }
    }

    private void showPopupWindow(final String title, final int period, double originPrice, final double price) {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(getApplicationContext(), R.layout.product_detail_pop_layout, null);

        TextView product_name = (TextView) popView.findViewById(R.id.product_name);
        TextView origin_price = (TextView) popView.findViewById(R.id.origin_price);
        TextView current_price = (TextView) popView.findViewById(R.id.current_price);
        TextView total_price = (TextView) popView.findViewById(R.id.total_price);
        product_name.setText(title);
        origin_price.setText("原价"+originPrice+"元");
        current_price.setText("现价"+price+"元");
        total_price.setText(""+price+"元");

        TextView confirm_purchase = (TextView) popView.findViewById(R.id.confirm_purchase);

        final PopupWindow popWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失

        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

        confirm_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                showPurchaseWindow(title, price, period);
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showPurchaseWindow(String title, final double price, final int period){
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(getApplicationContext(), R.layout.wechat_purchage_pop_layout, null);

        TextView taocan_title = (TextView) popView.findViewById(R.id.taocan_title);
        taocan_title.setText(title);
        TextView price_tv = (TextView) popView.findViewById(R.id.price);
        price_tv.setText(""+price+"元");
        TextView confirm_pay = (TextView) popView.findViewById(R.id.confirm_pay);

        final PopupWindow popWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失

        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

        confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPay(price,period);
                popWindow.dismiss();
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void confirmPay(double price, int period){
        showDialog("获取支付信息");
        BuyVIPServiceRequest request = new BuyVIPServiceRequest();
        request.setUser_id(userInfo.getUserId());
        request.setRequest_code(request_code_edit.getText().toString());
        request.setCost(0.01);
        request.setTime_limit(period);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                buyVIPServiceResponse = gson.fromJson(response,
                        BuyVIPServiceResponse.class);
                if (buyVIPServiceResponse !=null &&
                        buyVIPServiceResponse.getHandleResult().equals("OK")
                        && buyVIPServiceResponse.getOrder_result() == 1){
                    dismissDialog();
                    weChatPay(buyVIPServiceResponse);
                }else {
                    dismissDialog();
                    showErrorMsg("获取订单失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                dismissDialog();
                showNetWorkException();
            }
        });
    }

    private void weChatPay(BuyVIPServiceResponse response){
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), response.getAppid(), true);
        api.registerApp(response.getAppid());
        PayReq req = new PayReq();
        req.appId = response.getAppid();// 微信开放平台审核通过的应用APPID
        try {
            req.partnerId = response.getPartnerid();// 微信支付分配的商户号
            req.prepayId = response.getPrepayid();// 预支付订单号，app服务器调用“统一下单”接口获取
            req.nonceStr = response.getNoncestr();// 随机字符串，不长于32位，服务器小哥会给咱生成
            req.timeStamp = response.getTimestamp();// 时间戳，app服务器小哥给出
            req.packageValue = "Sign=WXPay";// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
            req.sign = response.getSign();// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
        } catch (Exception e) {
            e.printStackTrace();
        }
        api.sendReq(req);
        LogUtil.d(TAG, "发起微信支付申请");
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        boolean pay = intent.getBooleanExtra("支付完成", false);
        if (pay && buyVIPServiceResponse != null){
            getWXPayResult();
        }
    }

    private void displayPopupWindowTips(int imageResId){
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View layout = getLayoutInflater().inflate(R.layout.app_overlay_layout, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        layout.setBackgroundResource(imageResId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pop.setClippingEnabled(false);
        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.showAtLocation(parent, Gravity.TOP|Gravity.START, 0, 0);
    }

    private void getWXPayResult(){
        showDialog("获取支付结果");
        GetWXPayResultRequest request = new GetWXPayResultRequest();
        request.setPrepayid(buyVIPServiceResponse.getPrepayid());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetWXPayResultResponse getWXPayResultResponse = gson.fromJson(response,
                        GetWXPayResultResponse.class);
                if (getWXPayResultResponse !=null &&
                        getWXPayResultResponse.getHandleResult().equals("OK")
                        && getWXPayResultResponse.getPay_result() == 1){
                    dismissDialog();
                    displayPopupWindowTips(R.drawable.purchase_success);
                }else {
                    dismissDialog();
                    showErrorMsg("获取支付结果失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                dismissDialog();
                showNetWorkException();
            }
        });
    }
}
