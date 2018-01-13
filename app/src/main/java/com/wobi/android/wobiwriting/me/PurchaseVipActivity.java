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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.me.message.BuyVIPServiceRequest;
import com.wobi.android.wobiwriting.me.message.BuyVIPServiceResponse;
import com.wobi.android.wobiwriting.me.message.GetWXPayResultRequest;
import com.wobi.android.wobiwriting.me.message.GetWXPayResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchJoinedCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.SearchOwnedCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class PurchaseVipActivity extends ActionBarActivity implements View.OnClickListener,
        AbstractSpinnerAdapter.IOnItemSelectListener{

    public static final int REQUEST_CODE = 1050;
    public static final int RESULT_CODE_SUCCESS = 0x88;
    private static final String TAG = "PurchaseVipActivity";
    private final DecimalFormat df = new DecimalFormat("######0.00");
    private EditText request_code_edit;
    private UserGetInfoResponse userInfo;
    private BuyVIPServiceResponse buyVIPServiceResponse;
    private RequestCodeSpinnerPopWindow mSpinnerPopWindow;
    private RequestCodeSpinnerAdapter mAdapter;

    private List<CommunityInfo> communityInfos = new ArrayList<>();
    private Map<String, CommunityInfo> communityIds = new HashMap<>();
    private RelativeLayout request_code_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wodou_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        initViews();
        setCustomActionBar();
        initData();
    }

    private void initData(){
        searchJoinedCommunity();
    }

    private void searchJoinedCommunity(){
        SearchJoinedCommunityRequest request = new SearchJoinedCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                updateCommunities(response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void updateCommunities(String response){
        LogUtil.d(TAG," response: "+response);
        SearchCommunityResultResponse searchResponse = gson.
                fromJson(response,SearchCommunityResultResponse.class);
        if (searchResponse != null
                && searchResponse.getHandleResult().equals("OK")){
            if (searchResponse.getCommunityList() == null
                    || searchResponse.getCommunityList().size() == 0){
//                showErrorMsg("当前不存在圈子");
            }else {
                for (CommunityInfo communityInfo: searchResponse.getCommunityList()){
                    if (!communityIds.containsKey(communityInfo.getRequest_code())) {
                        communityInfos.add(communityInfo);
                        communityIds.put(communityInfo.getRequest_code(), communityInfo);
                    }
                }
                Collections.sort(communityInfos);
                mAdapter.notifyDataSetChanged();
                if (communityInfos.size()>=1) {
                    request_code_edit.setText(communityInfos.get(0).getRequest_code());
                }
            }
        }else {
            showErrorMsg("获取数据异常");
        }
    }

    private void initViews(){

        request_code_layout = (RelativeLayout)findViewById(R.id.request_code_layout);

        mAdapter = new RequestCodeSpinnerAdapter(getApplicationContext());
        mAdapter.refreshData(communityInfos, 0);

        mSpinnerPopWindow = new RequestCodeSpinnerPopWindow(getApplicationContext());
        mSpinnerPopWindow.setAdapter(mAdapter);
        mSpinnerPopWindow.setItemListener(this);

        TextView tao_can1_purchase = (TextView)findViewById(R.id.tao_can1_purchase);
        TextView tao_can2_purchase = (TextView)findViewById(R.id.tao_can2_purchase);
        TextView tao_can3_purchase = (TextView)findViewById(R.id.tao_can3_purchase);
        TextView tao_can4_purchase = (TextView)findViewById(R.id.tao_can4_purchase);

        tao_can1_purchase.setOnClickListener(this);
        tao_can2_purchase.setOnClickListener(this);
        tao_can3_purchase.setOnClickListener(this);
        tao_can4_purchase.setOnClickListener(this);

        request_code_edit = (EditText)findViewById(R.id.request_code_edit);

        request_code_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinWindow();
            }
        });

    }

    private void showSpinWindow(){
        mSpinnerPopWindow.setWidth(request_code_layout.getWidth());
        mSpinnerPopWindow.showAsDropDown(request_code_layout);
//        request_code_layout.setBackgroundResource(R.drawable.home_grade_title_selected_shape_corner);
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
        final double wobiMaxDiscount  = (userInfo.getWobiBeans()/10) > 0 ? userInfo.getWobiBeans()/10 : 0;
        final double price_need_pay = wobiMaxDiscount >= price ? 0: price-wobiMaxDiscount;
        final double usedDiscount = wobiMaxDiscount >= price ? price: wobiMaxDiscount;
        final double final_price_need_pay = Double.parseDouble(df.format(price_need_pay));
        if (wobiMaxDiscount > 0){
            total_price.setText(""+final_price_need_pay+"元" +"（沃豆抵扣"+usedDiscount+"元）");
        }else {
            total_price.setText(""+final_price_need_pay+"元");
        }


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
                showPurchaseWindow(title, final_price_need_pay, usedDiscount, period);
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showPurchaseWindow(String title, final double price, final double discount, final int period){
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
                confirmPay(price,discount,period);
                popWindow.dismiss();
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void confirmPay(double price, double discount, int period){
        showDialog("获取支付信息");
        BuyVIPServiceRequest request = new BuyVIPServiceRequest();
        request.setUser_id(userInfo.getUserId());
        request.setRequest_code(request_code_edit.getText().toString());
        request.setCost(price);
        request.setWobi_beans_cost(discount * 10);
        request.setTime_limit(period);
//        if (period == 24){
//            //test project 1
//            request.setCost(0.01);
//            request.setWobi_beans_cost(0);
//            request.setTime_limit(1);
//        }else if (period == 36){
//            //test project 2
//            request.setCost(0.01);
//            request.setWobi_beans_cost(100);
//            request.setTime_limit(2);
//        }
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
                showErrorMsg(errorMessage);
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

        boolean payCancel = intent.getBooleanExtra("用户取消", false);
        if (payCancel && buyVIPServiceResponse != null){
            showErrorMsg("用户取消");
        }

        boolean payException = intent.getBooleanExtra("一般错误", false);
        if (payException && buyVIPServiceResponse != null){
            showErrorMsg("一般错误");
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
                    updateUserInfo();
                }else {
                    dismissDialog();
                    showErrorMsg("获取支付结果失败");
                    updateUserInfo();
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                dismissDialog();
                showErrorMsg(errorMessage);
            }
        });
    }

    private void updateUserInfo(){
        UserGetInfoRequest request = new UserGetInfoRequest();
        request.setUserId(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserGetInfoResponse userGetInfoResponse = gson.fromJson(response, UserGetInfoResponse.class);
                if (userGetInfoResponse != null && userGetInfoResponse.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginInfo(getApplicationContext(),response);
                    userInfo = userGetInfoResponse;
                }else {
                    showErrorMsg("用户信息更新失败 "+ userGetInfoResponse.getHandleResult());
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        request_code_edit.setText(communityInfos.get(pos).getRequest_code());
    }
}
