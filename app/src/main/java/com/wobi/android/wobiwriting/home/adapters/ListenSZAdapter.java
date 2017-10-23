package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/21.
 */

public class ListenSZAdapter extends RecyclerView.Adapter<ListenSZAdapter.SelectedSZViewHolder> {

    private final Context mContext;
    private final List<GetSZInfoResponse> szInfoResponses;
    private final LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public ListenSZAdapter(Context context, List<GetSZInfoResponse> szInfoResponses){
        this.mContext = context;
        this.szInfoResponses = szInfoResponses;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }


    @Override
    public SelectedSZViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedSZViewHolder(mInflater.inflate(R.layout.listen_sz_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedSZViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return szInfoResponses.size();
    }

    public class SelectedSZViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView ordinal_number;
        private final TextView pinyin;
        private final TextView word;
        private final TextView zuci;

        public SelectedSZViewHolder(View itemView) {
            super(itemView);
            ordinal_number = (TextView)itemView.findViewById(R.id.ordinal_number);
            pinyin = (TextView)itemView.findViewById(R.id.pinyin);
            word = (TextView)itemView.findViewById(R.id.word);
            zuci = (TextView)itemView.findViewById(R.id.zuci);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            if (position%2 == 1){
                itemView.setBackgroundColor(Color.parseColor("#ededed"));
            }else if (position%2 == 0){
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            ordinal_number.setText(""+(position+1));
            pinyin.setText(szInfoResponses.get(position).getPinyin());
            word.setText(szInfoResponses.get(position).getWord());
            zuci.setText(szInfoResponses.get(position).getZuci());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
