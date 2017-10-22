package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/22.
 */

public class DirectorySzAdapter extends RecyclerView.Adapter<DirectorySzAdapter.SelectedSZHolder> {


    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<String> mData;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition = 0;


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public DirectorySzAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }

    public int getSelectedPosition(){
        return selectedPosition;
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

    public class SelectedSZHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView speak_sz_item_text;

        public SelectedSZHolder(View itemView) {
            super(itemView);
            speak_sz_item_text = (TextView) itemView.findViewById(R.id.directory_sz_item_text);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            itemView.setOnClickListener(this);
            if (selectedPosition == position){
                speak_sz_item_text.setBackgroundResource(R.drawable.kewen_directory_sz_background_selected);
                speak_sz_item_text.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            }else {
                speak_sz_item_text.setTextColor(mContext.getResources().getColor(android.R.color.black));
                speak_sz_item_text.setBackgroundResource(R.drawable.kewen_directory_sz_backgroud_normal);
            }
            speak_sz_item_text.setText(mData.get(position));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
