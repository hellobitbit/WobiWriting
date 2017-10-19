package com.wobi.android.wobiwriting.home;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wobi.android.wobiwriting.utils.LogUtil;

/**
 * Created by wangyingren on 2017/10/6.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static String TAG = "SpaceItemDecoration";
    private int mLeftSpace, mTopSpace;

    public SpaceItemDecoration(Context context, int leftSpace, int topSpace) {
        mLeftSpace = dip2px(context, leftSpace);
        mTopSpace =  dip2px(context, topSpace);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos != 0){
            outRect.left = mLeftSpace;
        }else {
            outRect.left = 0;
        }

        if (pos != 0) {
            outRect.top = mTopSpace;
        } else {
            outRect.top = 0;
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
