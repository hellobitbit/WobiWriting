package com.wobi.android.wobiwriting.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class TargetToolBar extends RelativeLayout{

    private ImageView mToolBarIcon;
    private boolean isSelected;

    private int mSelectedIconId;
    private int mNoSelectedIconId;

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
        a.recycle();
    }

    private void inflate(Context context){
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tool_bar_layout, this);
        mToolBarIcon = (ImageView)findViewById(R.id.toolBarIcon);
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

    private void updateToolBarStatus(){

    }
}
