package com.wobi.android.wobiwriting.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.moments.ImagePickerAdapter;
import com.wobi.android.wobiwriting.ui.CustomActionBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/19.
 */

public abstract class BaseVideoActivity extends CustomActionBarActivity
        implements TitlePickerAdapter.OnRecyclerViewItemClickListener{

    private TitlePickerAdapter adapter;
    protected List<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cn_classic_layout);
        initViews();

    }

    private void initViews() {
        RecyclerView titleRecycler = (RecyclerView)findViewById(R.id.titleRecycler);
        adapter = new TitlePickerAdapter(this, mTitles);
        adapter.setOnItemClickListener(this);

        titleRecycler.setLayoutManager(new GridLayoutManager(this, mTitles.size()));
        titleRecycler.setHasFixedSize(true);
        titleRecycler.setAdapter(adapter);
    }
}
