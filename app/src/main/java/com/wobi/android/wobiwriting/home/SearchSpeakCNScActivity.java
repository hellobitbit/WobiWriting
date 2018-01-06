package com.wobi.android.wobiwriting.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.MainSCDisplayAdapter;
import com.wobi.android.wobiwriting.home.adapters.SCSpeakTypeAdapter;
import com.wobi.android.wobiwriting.home.adapters.SpeakSZAdapter;
import com.wobi.android.wobiwriting.home.adapters.SpeakTypeAdapter;
import com.wobi.android.wobiwriting.home.message.GetSCInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSCInfoResponse;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.video.UniversalMediaController;
import com.wobi.android.wobiwriting.video.UniversalVideoView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangyingren on 2017/11/28.
 */

public class SearchSpeakCNScActivity extends ActionBarActivity
        implements SpeakTypeAdapter.OnRecyclerViewItemClickListener, SpeakSZAdapter.OnRecyclerViewItemClickListener{
    public static final String SPEAK_TYPE ="speak_type";
    public static final String SZ_LIST ="sz_list";
    private static final String TAG = "SearchSpeakCNScActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private ArrayList<String> szList = new ArrayList<>();
    private RecyclerView mSpeakTypeRecyclerView;
    private SCSpeakTypeAdapter mAdapter;
    private int speakTypeValue = 0;
    private GetSCInfoResponse currentScInfo;

    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;
    private FrameLayout mVideoLayout;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private RelativeLayout speak_custom_action_bar;
    private TextView introduction_content;
    private TextView change_sc;
    private RecyclerView sc_recycler;
    private MainSCDisplayAdapter scAdapter;


    public enum SpeakType{
        BANSHU(0),
        YINGBI(1),
        MAOBI(2);

        private int mValue;

        SpeakType(int value){
            mValue = value;
        }

        public int getValue(){
            return mValue;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_cn_sc_layout);
        speakTypeValue = getIntent().getIntExtra(SPEAK_TYPE, 0);
        szList = getIntent().getStringArrayListExtra(SZ_LIST);
        initViews();
        setCustomActionBar();
        if (szList.size() > 0) {
            updateTitleText(szList.get(0));
            loadSCInfo(szList.get(0));
        }
    }

    private void initViews() {
        //得到控件
        mSpeakTypeRecyclerView = (RecyclerView) findViewById(R.id.speakTypeRecycler);
        mSpeakTypeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mSpeakTypeRecyclerView.setHasFixedSize(false);
        //设置适配器
        mAdapter = new SCSpeakTypeAdapter(this, true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setSelected(speakTypeValue);
        mSpeakTypeRecyclerView.smoothScrollToPosition(speakTypeValue);
        mSpeakTypeRecyclerView.setAdapter(mAdapter);

        sc_recycler = (RecyclerView) findViewById(R.id.sc_recycler);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(SearchSpeakCNScActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        sc_recycler.setLayoutManager(linearLayoutManager1);
        sc_recycler.addItemDecoration(new SpaceItemDecoration(SearchSpeakCNScActivity.this, 3, 0));
        sc_recycler.setHasFixedSize(true);
        scAdapter = new MainSCDisplayAdapter(SearchSpeakCNScActivity.this);
        sc_recycler.setAdapter(scAdapter);

        introduction_content = (TextView)findViewById(R.id.introduction_content);

        change_sc = (TextView) findViewById(R.id.change_sc);
        change_sc.setVisibility(View.GONE);
        initVideo();
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    private void loadSCInfo(final String text){
        GetSCInfoRequest request = new GetSCInfoRequest();
        request.setSc(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSCInfoResponse scInfoResponse = gson.fromJson(response, GetSCInfoResponse.class);
                if (scInfoResponse != null && scInfoResponse.getHandleResult().equals("OK")){
                    currentScInfo = scInfoResponse;
                    updateScRelatedUI(scInfoResponse);
                }else {
                    showErrorMsg("获取生词信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void updateScRelatedUI(GetSCInfoResponse scInfoResponse){

        scAdapter.updateInfo(scInfoResponse);
        scAdapter.notifyDataSetChanged();
        introduction_content.setText(scInfoResponse.getPretation());

        refreshVideoPlay(scInfoResponse);
    }

    private void refreshVideoPlay(GetSCInfoResponse scInfoResponse){
        //play media
        if (speakTypeValue == SearchSpeakCNScActivity.SpeakType.BANSHU.getValue()){
            String url = scInfoResponse.getBanshu_url();
            play(url);
        }else if (speakTypeValue == SearchSpeakCNScActivity.SpeakType.YINGBI.getValue()){
            String url = scInfoResponse.getYingbi_url();
            play(url);
        }else if (speakTypeValue == SearchSpeakCNScActivity.SpeakType.MAOBI.getValue()){
            String url = scInfoResponse.getMaobi_url();
            play(url);
        }
    }

    private void play(String url){
        LogUtil.d(TAG," play url = "+url);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
        mVideoView.requestFocus();

        mMediaController.setTitle(szList.get(0));
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.speakType){
            mAdapter.setSelected(position);
            updateSpeakType(position);
            mAdapter.notifyDataSetChanged();
            if (currentScInfo != null) {
                refreshVideoPlay(currentScInfo);
            }
        }
    }

    @Override
    protected void onClickActionBarImageButton(){
        startSearchActivity(1);
    }

    private void startSearchActivity(int type){
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.SEARCH_TYPE,"sc");
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    private void updateSpeakType(int position){
        speakTypeValue = position;
    }

    private void initVideo(){
        speak_custom_action_bar = (RelativeLayout)findViewById(R.id.speak_custom_action_bar);
        mVideoLayout = (FrameLayout)findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView)findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();

        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                SearchSpeakCNScActivity.this.isFullscreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoLayout.setLayoutParams(layoutParams);
                    //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
                    speak_custom_action_bar.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = cachedHeight;
                    mVideoLayout.setLayoutParams(layoutParams);
                    speak_custom_action_bar.setVisibility(View.VISIBLE);
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
                cachedHeight = mVideoLayout.getHeight();
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (SearchSpeakCNScActivity.this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        LogUtil.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }
}
