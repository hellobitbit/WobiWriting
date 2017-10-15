package com.wobi.android.wobiwriting.ui.adpaters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.wobi.android.wobiwriting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/15.
 */

public class GuideViewpagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews = new ArrayList<>();

    public GuideViewpagerAdapter(Context context, int imgIds[]){
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imgIds[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    //是否复用当前view对象
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //初始化每个条目要显示的内容
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = imageViews.get(position);
        container.addView(view);
        return view;
    }

    //销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }
}
