package com.wobi.android.wobiwriting.home;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/19.
 */

public abstract class BaseVideoActivity extends ActionBarActivity
        implements TitlePickerAdapter.OnRecyclerViewItemClickListener{
    static final int CN_CLASSIC = 0;
    static final int CALLIGRAGHY_CLASS = 1;
    private static final String TAG = "BaseVideoActivity";
    private static final int REQUEST_CODE = 1005;
    protected TitlePickerAdapter adapter;
    protected List<String> mTitles = new ArrayList<>();

    protected AbstractDirectoryAdapter mAdapter;

    protected Gson gson = new Gson();
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cn_classic_layout);
        initViews();
        setCustomActionBar();
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

    protected void startSearchActivity(int type){
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    protected void checkLogin(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("登录后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == REQUEST_CODE
                && resultCode == LoginActivity.RESULT_CODE_SUCCESS){
            mAdapter.notifyDataSetChanged();
        }
    }

    protected abstract void initDirectory();
}
