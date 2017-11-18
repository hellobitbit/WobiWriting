package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/8.
 */

public abstract class AbstractDirectoryAdapter<T> extends
        RecyclerView.Adapter<AbstractDirectoryAdapter.DirectoryViewHolder> {

    protected Gson gson = new Gson();
    private final Context mContext;
    protected final LayoutInflater mInflater;
    protected List<T> mDirectories;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(AbstractDirectoryAdapter.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelected(int position){
        this.selectedPosition = position;
    }

    public AbstractDirectoryAdapter(Context context, List<T> directories) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDirectories = directories;
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView directory_icon;
        private final ImageView directory_arrow;
        private final TextView title_view;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.directory_title);
            directory_icon = (ImageView) itemView.findViewById(R.id.directory_icon);
            directory_arrow = (ImageView) itemView.findViewById(R.id.directory_arrow);
        }

        public void bind(int position) {
            boolean isLogout = TextUtils.isEmpty(SharedPrefUtil.getLoginInfo(mContext));
            if (selectedPosition == position){
                directory_icon.setImageResource(R.drawable.directory_icon_red_open);
                title_view.setTextColor(Color.parseColor("#fc5c59"));
                directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
            }else {
                if (isLogout){
                    if (position == 0){
                        directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
                        directory_icon.setImageResource(R.drawable.directory_icon_red_closed);
                    }else {
                        directory_icon.setImageResource(R.drawable.kewen_directory_icon);
                        directory_arrow.setImageResource(R.drawable.directory_arrow_right_default);
                    }
                    title_view.setTextColor(Color.parseColor("#b0b0b0"));
                }else {
                    String userInfo = SharedPrefUtil.getLoginInfo(mContext);
                    UserGetInfoResponse userObj = gson.fromJson(userInfo, UserGetInfoResponse.class);
                    title_view.setTextColor(Color.parseColor("#b0b0b0"));
                    if (userObj.getIs_vip() == 1 || (userObj.getIs_vip() == 0
                            && (position == 0 || position == 1 || position ==2 ))){
                        directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
                        directory_icon.setImageResource(R.drawable.directory_icon_red_closed);
                    }else {
                        directory_icon.setImageResource(R.drawable.kewen_directory_icon);
                        directory_arrow.setImageResource(R.drawable.directory_arrow_right_default);
                    }

                }
            }
            itemView.setTag(position);
            itemView.setOnClickListener(this);
            updateTitleUI(title_view, position);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }

    public abstract void updateTitleUI(TextView title_view, int position);
}
