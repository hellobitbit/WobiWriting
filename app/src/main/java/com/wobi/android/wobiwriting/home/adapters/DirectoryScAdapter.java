package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/11/28.
 */

public class DirectoryScAdapter extends DirectoryContentAdapter{

    public DirectoryScAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    void updateTextView(TextView textView, boolean isSelected) {
        if (isSelected){
            textView.setBackgroundColor(Color.parseColor("#fa5453"));
            textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }else {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            textView.setBackgroundColor(Color.parseColor("#f8f8f8"));
        }
    }

    @Override
    public SelectedSZHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedSZHolder(mInflater.inflate(R.layout.directory_sc_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedSZHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
