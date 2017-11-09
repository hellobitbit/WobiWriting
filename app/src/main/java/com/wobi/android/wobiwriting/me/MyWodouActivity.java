package com.wobi.android.wobiwriting.me;

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

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyWodouActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MyWodouActivity";
    private EditText request_code_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wodou_layout);
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

    private void showPopupWindow(final String title, int period, double originPrice, final double price) {
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
                showPurchaseWindow(title, price);
            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showPurchaseWindow(String title, double price){
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

            }
        });

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
