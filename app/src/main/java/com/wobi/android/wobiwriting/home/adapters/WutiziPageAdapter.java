package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/11/11.
 */

public class WutiziPageAdapter extends PagerAdapter {

    private static final String TAG = "WutiziPageAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;

    private List<String> mData = new ArrayList<>();
    private int selectedPosition = 0;
    private GetSZInfoResponse szInfo;

    public WutiziPageAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(15))
                .build();
    }

    public void setSelected(int position){
        selectedPosition = position;
        if (szInfo != null) {
            if (selectedPosition == 0) {
                mData.clear();
                mData.addAll(szInfo.getAuthor1List());
            }else if (selectedPosition == 1){
                mData.clear();
                mData.addAll(szInfo.getAuthor2List());
            }else if (selectedPosition == 2){
                mData.clear();
                mData.addAll(szInfo.getAuthor3List());
            }else if (selectedPosition == 3){
                mData.clear();
                mData.addAll(szInfo.getAuthor4List());
            }else if (selectedPosition == 4){
                mData.clear();
                mData.addAll(szInfo.getAuthor5List());
            }

            this.notifyDataSetChanged();
        }
    }

    public void setSzInfo(GetSZInfoResponse szInfo){
        this.szInfo = szInfo;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        if (mData != null && position < mData.size()) {
            String text = mData.get(position);
            if (!TextUtils.isEmpty(text)) {
                View itemView  = mInflater.inflate(R.layout.wuti_info_list_item_layout,
                        null, false);
                ImageView wutizi_icon = (ImageView) itemView.findViewById(R.id.wutizi_icon);
                TextView author = (TextView) itemView.findViewById(R.id.author);
                author.setText(mData.get(position));
                if (szInfo != null) {
                    String url = szInfo.getWutizi_url()+szInfo.getFile()+"/"+(selectedPosition+1)
                            +"/"+"b"+(position+1)+".png";
                    LogUtil.d(TAG,"bind url = "+url);
                    imageLoader.displayImage(url, wutizi_icon, options);
                }
                //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                itemView.setTag(text);

                ((ViewPager) container).addView(itemView);
                return itemView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
