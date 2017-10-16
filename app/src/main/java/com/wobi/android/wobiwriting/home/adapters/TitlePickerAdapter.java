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
 * Created by wangyingren on 2017/9/19.
 */

public class TitlePickerAdapter extends RecyclerView.Adapter<TitlePickerAdapter.SelectedTitleViewHolder>{

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

    public TitlePickerAdapter(Context context, List<String> data) {
        this.mContext = context;
        mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }


    @Override
    public SelectedTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedTitleViewHolder(mInflater.inflate(R.layout.home_title_list_item_layout,
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
                selectedLineView.setVisibility(View.VISIBLE);
            }else {
                selectedLineView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
