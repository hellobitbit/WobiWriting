package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wobi.android.wobiwriting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/15.
 */

public class BannerViewpagerAdapter extends PagerAdapter {

    private int imgIds[] = {R.drawable.banner, R.drawable.banner2, R.drawable.banner3};
    private List<ImageView> imageViews = new ArrayList<>();

    public BannerViewpagerAdapter(Context context){
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imgIds[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    //是否复用当前view对象
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //初始化每个条目要显示的内容
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        position %= imageViews.size();
        if (position<0){
            position = imageViews.size()+position;
        }
        ImageView view = imageViews.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    //销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //do nothing
    }
}
