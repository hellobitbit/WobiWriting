package com.wobi.android.wobiwriting.moments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.utils.DateUtils;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/23.
 */

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.SelectedSZViewHolder> {

    private final Context mContext;
    private final List<CommunityInfo> communityInfos;
    private final LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public MomentsAdapter(Context context, List<CommunityInfo> communityInfos){
        this.mContext = context;
        this.communityInfos = communityInfos;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }


    @Override
    public SelectedSZViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedSZViewHolder(mInflater.inflate(R.layout.moment_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedSZViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return communityInfos.size();
    }

    public class SelectedSZViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView moment_icon;
        private final TextView moment_name;
        private final TextView moment_code;
        private final TextView moment_time;
        private final TextView moment_summary;
        private final RelativeLayout favor_layout;
        private final RelativeLayout comments_layout;
        private final RelativeLayout forward_layout;
        private final TextView favor;
        private final TextView comments;
        private final TextView forward;
        private final TextView moment_type;

        public SelectedSZViewHolder(View itemView) {
            super(itemView);
            moment_icon = (ImageView)itemView.findViewById(R.id.moment_icon);
            moment_name = (TextView)itemView.findViewById(R.id.moment_name);
            moment_code = (TextView)itemView.findViewById(R.id.moment_code);
            moment_time = (TextView)itemView.findViewById(R.id.moment_time);
            moment_summary = (TextView)itemView.findViewById(R.id.moment_summary);

            favor_layout = (RelativeLayout)itemView.findViewById(R.id.favor_layout);
            comments_layout = (RelativeLayout)itemView.findViewById(R.id.comments_layout);
            forward_layout = (RelativeLayout)itemView.findViewById(R.id.forward_layout);
            favor = (TextView)itemView.findViewById(R.id.favor);
            comments = (TextView)itemView.findViewById(R.id.comments);
            forward = (TextView)itemView.findViewById(R.id.forward);

            moment_type = (TextView)itemView.findViewById(R.id.moment_type);
        }

        public void bind(int position) {
            //设置条目的点击事件
            if (position == 0){
                moment_type.setVisibility(View.VISIBLE);
            }else {
                moment_type.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
            itemView.setTag(position);
            moment_name.setText(communityInfos.get(position).getCommunity_name());
            moment_code.setText("邀请码："+communityInfos.get(position).getRequest_code());
            moment_summary.setText(communityInfos.get(position).getSummary());
            moment_time.setText(DateUtils.parseDateString(communityInfos.get(position).getCreate_time()));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
