package com.wobi.android.wobiwriting.moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CommunityListAdapter extends BaseAdapter {

    private final Context mContext;

    public CommunityListAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.moment_item_layout,null);
        }
        return convertView;
    }
}
