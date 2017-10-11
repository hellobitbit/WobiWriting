package com.wobi.android.wobiwriting.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by wangyingren on 2017/10/10.
 */

public class CustomVideoVIew extends VideoView {
    public CustomVideoVIew(Context context) {
        super(context);
    }

    public CustomVideoVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = getDefaultSize(0, widthMeasureSpec);
//        int height = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(width, height);
//    }
}
