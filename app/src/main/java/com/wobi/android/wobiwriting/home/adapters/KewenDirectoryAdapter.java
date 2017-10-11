package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;

import java.util.List;

/**
 * Created by wangyingren on 2017/10/5.
 */

public class KewenDirectoryAdapter extends AbstractDirectoryAdapter<KeWenDirectory>{

    public KewenDirectoryAdapter(Context context, List<KeWenDirectory> directories) {
        super(context, directories);
    }

    @Override
    public void updateTitleUI(TextView title_view, int position) {
        title_view.setText(mDirectories.get(position).getKewen());
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KewenDirectoryAdapter.DirectoryViewHolder(mInflater.
                inflate(R.layout.kewen_directory_list_item_layout,
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
