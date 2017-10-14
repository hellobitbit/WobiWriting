package com.wobi.android.wobiwriting.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class TargetToolBar extends RelativeLayout{

    private ImageView mToolBarIcon;
    private boolean isSelected;
    private TextView mToolBarText;

    private int mSelectedIconId;
    private int mNoSelectedIconId;
    private int mTextValue;


    public TargetToolBar(Context context) {
        this(context, null);
    }

    public TargetToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TargetToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOptions(attrs, context);
        inflate(context);
    }

    private void initOptions(AttributeSet attrs, Context context){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TargetToolBar);
        isSelected = a.getBoolean(R.styleable.TargetToolBar_isSelected, false);
        mSelectedIconId = a.getResourceId(R.styleable.TargetToolBar_SelectedSrc, 0);
        mNoSelectedIconId = a.getResourceId(R.styleable.TargetToolBar_NoSelectedSrc, 0);
        mTextValue = a.getResourceId(R.styleable.TargetToolBar_textValue, 0);
        a.recycle();
    }

    private void inflate(Context context){
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tool_bar_layout, this);
        mToolBarIcon = (ImageView)findViewById(R.id.toolBarIcon);
        mToolBarText = (TextView)findViewById(R.id.toolBarText);
        ((TextView)findViewById(R.id.toolBarText)).setText(mTextValue);
        updateToolBarStatus();
    }

    public void setSelected(){
        isSelected = true;
        updateToolBarStatus();
    }

    public void setNoSelected(){
        isSelected = false;
        updateToolBarStatus();
    }

    public boolean isSelected(){
        return isSelected;
    }

    private void updateToolBarStatus(){
        if (isSelected){
            mToolBarIcon.setImageResource(mSelectedIconId);
            mToolBarText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else {
            mToolBarIcon.setImageResource(mNoSelectedIconId);
            mToolBarText.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}
