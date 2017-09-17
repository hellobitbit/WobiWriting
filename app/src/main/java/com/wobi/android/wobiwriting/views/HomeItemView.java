package com.wobi.android.wobiwriting.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/16.
 */

public class HomeItemView extends LinearLayout {
    private int mItemTypeNameRes;
    private int mItemSubType1NameRes;
    private int mItemSubType2NameRes;
    private int mItemSubType3NameRes;
    private int mItemSubType4NameRes;
    private int mItemTypeIconRes;

    public HomeItemView(Context context) {
        this(context, null);
    }

    public HomeItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOptions(attrs, context);
        inflate(context);
    }

    private void initOptions(AttributeSet attrs, Context context){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeItemView);
        mItemTypeIconRes = a.getResourceId(R.styleable.HomeItemView_itemTypeIcon, 0);
        mItemTypeNameRes = a.getResourceId(R.styleable.HomeItemView_itemTypeName, 0);
        mItemSubType1NameRes = a.getResourceId(R.styleable.HomeItemView_itemSubType1Name, 0);
        mItemSubType2NameRes = a.getResourceId(R.styleable.HomeItemView_itemSubType2Name, 0);
        mItemSubType3NameRes = a.getResourceId(R.styleable.HomeItemView_itemSubType3Name, 0);
        mItemSubType4NameRes = a.getResourceId(R.styleable.HomeItemView_itemSubType4Name, 0);

        a.recycle();
    }

    private void inflate(Context context){
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_item_bar_layout, this);
        ImageView itemTypeIcon = (ImageView)findViewById(R.id.home_item_type_icon);
        TextView itemTypeName = (TextView)findViewById(R.id.home_item_type_name);
        TextView itemSubType1Name = (TextView)findViewById(R.id.home_item_sub_type_name_1);
        TextView itemSubType2Name = (TextView)findViewById(R.id.home_item_sub_type_name_2);
        TextView itemSubType3Name = (TextView)findViewById(R.id.home_item_sub_type_name_3);
        TextView itemSubType4Name = (TextView)findViewById(R.id.home_item_sub_type_name_4);
        View intervalBelow = findViewById(R.id.interval_below);
        View intervalAbove = findViewById(R.id.interval_above);
        itemTypeIcon.setImageResource(mItemTypeIconRes);
        itemTypeName.setText(mItemTypeNameRes);
        itemSubType1Name.setText(mItemSubType1NameRes);
        itemSubType3Name.setText(mItemSubType3NameRes);
        if (mItemSubType2NameRes != 0){
            itemSubType2Name.setText(mItemSubType2NameRes);
            itemSubType2Name.setVisibility(VISIBLE);
            intervalAbove.setVisibility(VISIBLE);
        }else {
            itemSubType2Name.setVisibility(GONE);
            intervalAbove.setVisibility(GONE);
        }

        if (mItemSubType4NameRes != 0){
            itemSubType4Name.setText(mItemSubType4NameRes);
            itemSubType4Name.setVisibility(VISIBLE);
            intervalBelow.setVisibility(VISIBLE);
        }else {
            itemSubType4Name.setVisibility(GONE);
            intervalBelow.setVisibility(GONE);
        }
    }
}
