package com.wobi.android.wobiwriting.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.adapters.AbstractDirectoryAdapter;
import com.wobi.android.wobiwriting.home.adapters.TitlePickerAdapter;
import com.wobi.android.wobiwriting.me.PurchaseVipActivity;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.video.UniversalMediaController;
import com.wobi.android.wobiwriting.video.UniversalVideoView;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/19.
 */

public abstract class BaseVideoActivity extends ActionBarActivity
        implements TitlePickerAdapter.OnRecyclerViewItemClickListener {
    static final int CN_CLASSIC = 0;
    static final int CALLIGRAGHY_CLASS = 1;
    private static final String TAG = "BaseVideoActivity";
    private static final int REQUEST_CODE = 1005;
    protected TitlePickerAdapter adapter;
    protected List<String> mTitles = new ArrayList<>();

    protected AbstractDirectoryAdapter mAdapter;

    protected Gson gson = new Gson();
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    private FrameLayout mVideoLayout;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    RecyclerView titleRecycler;
    private RelativeLayout custom_action_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cn_classic_layout);
        initViews();
        setCustomActionBar();
    }

    private void initViews() {
        titleRecycler = (RecyclerView)findViewById(R.id.titleRecycler);
        adapter = new TitlePickerAdapter(this, mTitles);
        adapter.setOnItemClickListener(this);

        titleRecycler.setLayoutManager(new GridLayoutManager(this, mTitles.size()));
        titleRecycler.setHasFixedSize(true);
        titleRecycler.setAdapter(adapter);

        custom_action_bar = (RelativeLayout)findViewById(R.id.custom_action_bar);
        mVideoLayout = (FrameLayout)findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView)findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();

        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                BaseVideoActivity.this.isFullscreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoLayout.setLayoutParams(layoutParams);
                    //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
                    titleRecycler.setVisibility(View.GONE);
                    custom_action_bar.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = cachedHeight;
                    mVideoLayout.setLayoutParams(layoutParams);
                    titleRecycler.setVisibility(View.VISIBLE);
                    custom_action_bar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // 视频暂停
                LogUtil.d(TAG, "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
                LogUtil.d(TAG, "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// 视频开始缓冲
                LogUtil.d(TAG, "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// 视频结束缓冲
                LogUtil.d(TAG, "onBufferingEnd UniversalVideoView callback");
            }

        });
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }

    protected void play(String url, String title){
        LogUtil.d(TAG," play = "+url);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
        mVideoView.requestFocus();

        mMediaController.setTitle(title);
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

    protected void checkVip(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("充值后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去充值", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getApplicationContext(), PurchaseVipActivity.class);
                startActivityForResult(intent, PurchaseVipActivity.REQUEST_CODE);
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
        }else if (requestCode == PurchaseVipActivity.REQUEST_CODE){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (BaseVideoActivity.this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    protected abstract void initDirectory();
}
