package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.model.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/24.
 */

public abstract class AbstractSpinnerAdapter<T> extends BaseAdapter {

    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    private Context mContext;
    private List<T> mObjects = new ArrayList<T>();
    private int mSelectItem = 0;

    protected LayoutInflater mInflater;

    public AbstractSpinnerAdapter(Context context) {
        init(context);
    }

    public void refreshData(List<T> objects, int selIndex) {
        mObjects = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }

        mSelectItem = selIndex;
    }

    private void init(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        return mObjects.size();
    }

    @Override
    public T getItem(int pos) {
        return mObjects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }
}
