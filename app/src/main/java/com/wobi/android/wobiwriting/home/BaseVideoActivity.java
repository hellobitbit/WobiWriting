package com.wobi.android.wobiwriting.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.adapters.AbstractDirectoryAdapter;
import com.wobi.android.wobiwriting.home.adapters.TitlePickerAdapter;
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

    protected AbstractDirectoryAdapter mAdapter;

    protected Gson gson = new Gson();
    private VideoView mVideoView;

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

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setMediaController(new MediaController(this));
    }

    protected void play(String url){
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
        mVideoView.requestFocus();

    }

    protected abstract void initDirectory();
}
