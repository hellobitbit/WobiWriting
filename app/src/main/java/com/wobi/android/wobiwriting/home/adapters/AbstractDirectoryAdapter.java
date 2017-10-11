package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/8.
 */

public abstract class AbstractDirectoryAdapter<T> extends
        RecyclerView.Adapter<AbstractDirectoryAdapter.DirectoryViewHolder> {

    private final Context mContext;
    protected final LayoutInflater mInflater;
    protected List<T> mDirectories;
    private ClassCourseDirectoryAdapter.OnRecyclerViewItemClickListener listener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(AbstractDirectoryAdapter.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public AbstractDirectoryAdapter(Context context, List<T> directories) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDirectories = directories;
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title_view;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.directory_title);
        }

        public void bind(int position) {
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
