package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/11/28.
 */

public class DirectorySzAdapter extends DirectoryContentAdapter {

    public DirectorySzAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    void updateTextView(TextView textView, boolean isSelected) {
        if (isSelected){
            textView.setBackgroundResource(R.drawable.kewen_directory_sz_background_selected);
            textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        }else {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            textView.setBackgroundResource(R.drawable.kewen_directory_sz_backgroud_normal);
        }
    }

    @Override
    public SelectedSZHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedSZHolder(mInflater.inflate(R.layout.directory_sz_item_layout,
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
