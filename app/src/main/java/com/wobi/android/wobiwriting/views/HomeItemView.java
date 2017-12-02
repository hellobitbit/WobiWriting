package com.wobi.android.wobiwriting.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.HomeFragment;

/**
 * Created by wangyingren on 2017/9/16.
 */

public class HomeItemView extends LinearLayout {
    public static final String SUB_TYPE = "sub_type";
    public static final String SUB_TYPE_1 = "sub_type_1";
    public static final String SUB_TYPE_2 = "sub_type_2";
    public static final String SUB_TYPE_3 = "sub_type_3";
    public static final String SUB_TYPE_4 = "sub_type_4";
    private int mItemTypeNameRes;
    private int mItemSubType1NameRes;
    private int mItemSubType2NameRes;
    private int mItemSubType3NameRes;
    private int mItemSubType4NameRes;
    private int mItemTypeIconRes;
    private RelativeLayout mainLayout;
    private TextView itemTypeName;
    private TextView itemSubType1Name;
    private TextView itemSubType2Name;
    private TextView itemSubType3Name;
    private TextView itemSubType4Name;

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
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        ImageView itemTypeIcon = (ImageView)findViewById(R.id.home_item_type_icon);
        itemTypeName = (TextView)findViewById(R.id.home_item_type_name);
        itemSubType1Name = (TextView)findViewById(R.id.home_item_sub_type_name_1);
        itemSubType2Name = (TextView)findViewById(R.id.home_item_sub_type_name_2);
        itemSubType3Name = (TextView)findViewById(R.id.home_item_sub_type_name_3);
        itemSubType4Name = (TextView)findViewById(R.id.home_item_sub_type_name_4);
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

    public void setMainIntent(final Intent intent, final boolean support){
        mainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support) {
                    intent.putExtra(SUB_TYPE, SUB_TYPE_1);
                    getContext().startActivity(intent);
                }else {
                    showMessage();
                }
            }
        });
    }

    public void setSub1Intent(final Intent intent, final boolean support){

        itemSubType1Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support) {
                    intent.putExtra(SUB_TYPE, SUB_TYPE_1);
                    getContext().startActivity(intent);
                }else {
                    showMessage();
                }
            }
        });
    }

    public void setSub2Intent(final Intent intent, final boolean support){
        itemSubType2Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support){
                    intent.putExtra(SUB_TYPE, SUB_TYPE_2);
                    getContext().startActivity(intent);
                }else {
                    showMessage();
                }
            }
        });
    }

    public void setSub3Intent(final Intent intent, final boolean support){
        itemSubType3Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support){
                    intent.putExtra(SUB_TYPE, SUB_TYPE_3);
                    getContext().startActivity(intent);
                }else {
                    showMessage();
                }
            }
        });
    }

    public void setSub3Intent(final Intent intent, final boolean support, final boolean notGrade){
        itemSubType3Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support){
                    intent.putExtra(SUB_TYPE, SUB_TYPE_3);
                    getContext().startActivity(intent);
                }else {
                    if (notGrade){
                        showUnsupportMessage();
                    }else {
                        showMessage();
                    }
                }
            }
        });
    }

    public void setSub4Intent(final Intent intent, final boolean support){
        itemSubType4Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (support){
                    intent.putExtra(SUB_TYPE, SUB_TYPE_4);
                    getContext().startActivity(intent);
                }else {
                    showMessage();
                }
            }
        });
    }

    public void updateItem1Visibility(boolean visible){
        if (visible){
            itemSubType1Name.setVisibility(VISIBLE);
            findViewById(R.id.interval_above).setVisibility(VISIBLE);
        }else {
            itemSubType1Name.setVisibility(GONE);
            findViewById(R.id.interval_above).setVisibility(GONE);
        }
    }

    private void showMessage(){
        Toast.makeText(getContext(),"当前版本不支持该功能", Toast.LENGTH_LONG).show();
    }

    private void showUnsupportMessage(){
        Toast.makeText(getContext(),"该年级不支持该功能", Toast.LENGTH_LONG).show();
    }
}
