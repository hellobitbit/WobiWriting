package com.wobi.android.wobiwriting.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/9/16.
 */

public class CustomSettingBar extends RelativeLayout {
    private boolean mHaveLeftIcon;
    private int mLeftIconId;
    private int mRightIconId;
    private int mLeftIconMarginLeft;
    private int mLeftIconSize;
    private int mRightIconMarginRight;
    private int mRightIconSize;
    private int mLeftTextValue;
    private int mLeftTextColor;
    private int mLeftTextMarginLeft;
    private int mLeftTextSize;
    private int mRightTextColor;
    private int mRightTextValue;
    private int mRightTextMarginRight;
    private int mRightTextSize;
    private boolean mHaveRightIcon;

    public CustomSettingBar(Context context) {
        this(context, null);
    }

    public CustomSettingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSettingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOptions(attrs, context);
        inflate(context);
    }

    private void initOptions(AttributeSet attrs, Context context){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSettingBar);
        mHaveLeftIcon = a.getBoolean(R.styleable.CustomSettingBar_leftIconVisible, false);
        mLeftIconId = a.getResourceId(R.styleable.CustomSettingBar_leftIconSrc, 0);
        mLeftIconMarginLeft = a.getResourceId(R.styleable.CustomSettingBar_leftIconMarginLeft, 0);
        mLeftIconSize = a.getResourceId(R.styleable.CustomSettingBar_leftIconSize, 0);
        
        mRightIconId = a.getResourceId(R.styleable.CustomSettingBar_rightIconSrc, 0);
        mRightIconMarginRight = a.getResourceId(R.styleable.CustomSettingBar_rightIconMarginRight, 0);
        mRightIconSize = a.getResourceId(R.styleable.CustomSettingBar_rightIconSize, 0);
        mHaveRightIcon = a.getBoolean(R.styleable.CustomSettingBar_rightIconVisible, true);

        mLeftTextColor = a.getResourceId(R.styleable.CustomSettingBar_leftTextColor, 0);
        mLeftTextValue = a.getResourceId(R.styleable.CustomSettingBar_leftTextValue, 0);
        mLeftTextMarginLeft = a.getResourceId(R.styleable.CustomSettingBar_leftTextMarginLeft, 0);
        mLeftTextSize = a.getResourceId(R.styleable.CustomSettingBar_leftTextSize, 0);

        mRightTextColor = a.getResourceId(R.styleable.CustomSettingBar_rightTextColor, 0);
        mRightTextValue = a.getResourceId(R.styleable.CustomSettingBar_rightTextValue, 0);
        mRightTextMarginRight = a.getResourceId(R.styleable.CustomSettingBar_rightTextMarginRight, 0);
        mRightTextSize = a.getResourceId(R.styleable.CustomSettingBar_rightTextSize, 0);

        a.recycle();
    }

    private void inflate(Context context){
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.customer_setting_bar_layout, this);
        ImageView leftIcon = (ImageView)findViewById(R.id.leftIcon);
        if (mHaveLeftIcon){
            leftIcon.setVisibility(VISIBLE);
            if (mLeftIconId != 0){
                leftIcon.setImageResource(mLeftIconId);
            }
        }

        ImageView rightIcon = (ImageView)findViewById(R.id.rightIcon);
        if (mHaveRightIcon){
            rightIcon.setVisibility(VISIBLE);
        }else {
            rightIcon.setVisibility(INVISIBLE);
        }

        TextView leftView = (TextView)findViewById(R.id.leftText);
        if (mLeftTextValue != 0) {
            leftView.setText(mLeftTextValue);
        }

        if (mLeftTextColor != 0){
            leftView.setTextColor(getResources().getColor(mLeftTextColor));
        }

        TextView rightView = (TextView)findViewById(R.id.rightText);
        if (mRightTextValue != 0) {
            rightView.setText(mRightTextValue);
        }

        if (mRightTextColor != 0){
            rightView.setTextColor(getResources().getColor(mRightTextColor));
        }
    }

    public void setRightText(int resId){
        TextView rightTextView = (TextView)findViewById(R.id.rightText);
        rightTextView.setText(resId);
    }

    public void setRightText(String text){
        TextView rightTextView = (TextView)findViewById(R.id.rightText);
        rightTextView.setText(text);
    }

    public String getRightText(){
        TextView rightTextView = (TextView)findViewById(R.id.rightText);
        return rightTextView.getText().toString();
    }
}
