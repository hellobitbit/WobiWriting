package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/19.
 */

public class SpeakSZAdapter extends RecyclerView.Adapter<SpeakSZAdapter.SelectedTypeHolder> {

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

    public SpeakSZAdapter(Context context, List<String> data) {
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
    public SelectedTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedTypeHolder(mInflater.inflate(R.layout.speak_sz_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedTypeHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedTypeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView speak_sz_item_text;
        private final ImageView speak_sz_item_icon;

        public SelectedTypeHolder(View itemView) {
            super(itemView);
            speak_sz_item_text = (TextView) itemView.findViewById(R.id.speak_sz_item_text);
            speak_sz_item_icon = (ImageView) itemView.findViewById(R.id.speak_sz_item_icon);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            itemView.setOnClickListener(this);
            if (selectedPosition == position){
                speak_sz_item_text.setTextColor(mContext.getResources().getColor(android.R.color.white));
                speak_sz_item_icon.setImageResource(R.drawable.speak_sz_selected_background);
            }else {
                speak_sz_item_text.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                speak_sz_item_icon.setImageResource(R.drawable.speak_sz_normal_background);
            }
            speak_sz_item_text.setText(mData.get(position));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
