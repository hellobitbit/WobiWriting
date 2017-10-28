package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/28.
 */

public class WutiziTypeAdapter extends RecyclerView.Adapter<WutiziTypeAdapter.SelectedTitleViewHolder> {


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

    public WutiziTypeAdapter(Context context, List<String> data) {
        this.mContext = context;
        mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }

    public int getSelected(){
        return selectedPosition;
    }

    @Override
    public SelectedTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedTitleViewHolder(mInflater.inflate(R.layout.wuti_type_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedTitleViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedTitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title_view;
        private final View selectedLineView;

        public SelectedTitleViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.title);
            selectedLineView = (View) itemView.findViewById(R.id.selectedLine);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            title_view.setOnClickListener(this);
            title_view.setText(mData.get(position));
            if (position == selectedPosition){
                title_view.setTextColor(Color.parseColor("#f95453"));
                selectedLineView.setVisibility(View.VISIBLE);
            }else {
                title_view.setTextColor(Color.parseColor("#b7b7b7"));
                selectedLineView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
