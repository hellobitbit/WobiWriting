package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.model.CalligraphyClassCourse;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/8.
 */

public class ClassCourseDirectoryAdapter extends AbstractDirectoryAdapter<CalligraphyClassCourse> {

    public ClassCourseDirectoryAdapter(Context context, List<CalligraphyClassCourse> directories) {
        super(context, directories);
    }

    @Override
    public void updateTitleUI(TextView title_view, int position) {
        title_view.setText(mDirectories.get(position).getCourseName());
    }

    @Override
    public AbstractDirectoryAdapter.DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbstractDirectoryAdapter.DirectoryViewHolder(mInflater.
                inflate(R.layout.class_classic_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(AbstractDirectoryAdapter.DirectoryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDirectories.size();
    }
}
