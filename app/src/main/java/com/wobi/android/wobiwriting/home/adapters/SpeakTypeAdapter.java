package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/19.
 */

public class SpeakTypeAdapter extends RecyclerView.Adapter<SpeakTypeAdapter.SelectedTypeHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<Integer> mData;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition = 0;
    private ArrayList<Integer> mNormalDatas;
    private ArrayList<Integer> mSelectedDatas;


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public SpeakTypeAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        initDatas();
    }

    public void setSelected(int position){
        selectedPosition = position;
    }


    @Override
    public SelectedTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedTypeHolder(mInflater.inflate(R.layout.speak_type_item_layout,
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

        private final TextView speak_item_text;
        private final ImageView speak_item_icon;

        public SelectedTypeHolder(View itemView) {
            super(itemView);
            speak_item_text = (TextView) itemView.findViewById(R.id.speak_item_text);
            speak_item_icon = (ImageView) itemView.findViewById(R.id.speak_item_icon);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            itemView.setOnClickListener(this);
            if (selectedPosition == position){
                speak_item_text.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                speak_item_icon.setImageResource(mSelectedDatas.get(position));
            }else {
                speak_item_text.setTextColor(mContext.getResources().getColor(android.R.color.black));
                speak_item_icon.setImageResource(mNormalDatas.get(position));
            }
            speak_item_text.setText(mData.get(position));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }

    private void initDatas()
    {
        mNormalDatas = new ArrayList<>(Arrays.asList(R.drawable.speak_normal,
                R.drawable.speak_bi_normal, R.drawable.speak_ban_normal,
                R.drawable.speak_yin_normal, R.drawable.speak_mao_normal));

        mSelectedDatas = new ArrayList<>(Arrays.asList(R.drawable.speak_selected,
                R.drawable.speak_bi_selected, R.drawable.speak_ban_selected,
                R.drawable.speak_yin_selected, R.drawable.speak_mao_selected));

        mData = new ArrayList<>(Arrays.asList(R.string.home_item_speak,
                R.string.home_item_writing_bishun, R.string.home_item_writing_blackbroad,
                R.string.home_item_writing_hard, R.string.home_item_writing_brush));
    }
}
