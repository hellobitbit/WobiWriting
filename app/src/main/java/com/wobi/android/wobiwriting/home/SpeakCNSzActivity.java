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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.SpeakSZAdapter;
import com.wobi.android.wobiwriting.home.adapters.SpeakTypeAdapter;
import com.wobi.android.wobiwriting.home.adapters.WutiziInfoAdapter;
import com.wobi.android.wobiwriting.home.adapters.WutiziTypeAdapter;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.home.model.ListenSerializableMap;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.video.UniversalMediaController;
import com.wobi.android.wobiwriting.video.UniversalVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class SpeakCNSzActivity extends ActionBarActivity
        implements SpeakTypeAdapter.OnRecyclerViewItemClickListener, SpeakSZAdapter.OnRecyclerViewItemClickListener{
    public static final String GRADE_ID = "grade_id";
    public static final String SPEAK_TYPE ="speak_type";
    public static final String SZ_LIST ="sz_list";
    public static final String KEWEN_TITLE ="kewen_title";
    private static final String TAG = "SpeakCNSzActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_SUFFIX = ".mp4";
    private ArrayList<String> szList = new ArrayList<>();
    private HashMap<String, GetSZInfoResponse> szInfoResponseMap = new HashMap<>();
    private RecyclerView mSpeakTypeRecyclerView;
    private SpeakTypeAdapter mAdapter;
    private RecyclerView szListRecycler;
    private SpeakSZAdapter mSZAdapter;
    private int speakTypeValue = 0;
    private int originSpeakTypeValue = 0;
    private GetSZInfoResponse currentSzInfo;

    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;
    private FrameLayout mVideoLayout;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private RelativeLayout speak_custom_action_bar;
    private String kewenTitle;
    private RecyclerView wutiInfoRecycler;
    private WutiziInfoAdapter wutiziInfoAdapter;
    private List<String> wuzitiTypeList = new ArrayList<>();

    private RecyclerView wutiTypeRecycler;
    private WutiziTypeAdapter wutiziAdapter;
    private ImageButton wutizi_left_button;
    private ImageButton wutizi_right_button;

    public enum SpeakType{
        SWJZ(0),
        BISHUN(1),
        BANSHU(2),
        YINGBI(3),
        MAOBI(4),
        SWJZ_ZY(5),
        SWJZ_ZXYB(6),
        SWJZ_XXSY(7),
        SWJZ_GXGS(8);

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
        setContentView(R.layout.activity_speak_cn_layout);
        originSpeakTypeValue = getIntent().getIntExtra(SPEAK_TYPE, 0);
        if (originSpeakTypeValue == SpeakCNSzActivity.SpeakType.SWJZ_GXGS.getValue()
                || originSpeakTypeValue == SpeakCNSzActivity.SpeakType.SWJZ_XXSY.getValue()
                || originSpeakTypeValue == SpeakCNSzActivity.SpeakType.SWJZ_ZY.getValue()
                || originSpeakTypeValue == SpeakCNSzActivity.SpeakType.SWJZ_ZXYB.getValue()){
            speakTypeValue  = 0;
        }else {
            speakTypeValue =  originSpeakTypeValue;
        }
        szList = getIntent().getStringArrayListExtra(SZ_LIST);
        kewenTitle = getIntent().getStringExtra(KEWEN_TITLE);
        initViews();
        setCustomActionBar();
        updateTitleText(kewenTitle);

        int szPosition = SharedPrefUtil.getSZPosition(getApplicationContext());
        switchSz(szList.size()> szPosition ? szPosition : 0);
    }

    private void initViews() {
        //得到控件
        mSpeakTypeRecyclerView = (RecyclerView) findViewById(R.id.speakTypeRecycler);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSpeakTypeRecyclerView.setLayoutManager(linearLayoutManager);
        mSpeakTypeRecyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 30, 0));
        mSpeakTypeRecyclerView.setHasFixedSize(true);
        //设置适配器
        mAdapter = new SpeakTypeAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setSelected(speakTypeValue);
        mSpeakTypeRecyclerView.smoothScrollToPosition(speakTypeValue);
        mSpeakTypeRecyclerView.setAdapter(mAdapter);


        //得到控件
        szListRecycler = (RecyclerView) findViewById(R.id.szListRecycler);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        szListRecycler.setLayoutManager(linearLayoutManager1);
        szListRecycler.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 13, 0));
        szListRecycler.setHasFixedSize(true);
        //设置适配器
        mSZAdapter = new SpeakSZAdapter(this,szList);
        mSZAdapter.setOnItemClickListener(this);
        szListRecycler.setAdapter(mSZAdapter);

        ImageView listen = (ImageView)findViewById(R.id.listenAndWriting);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListenActivity.class);
                ListenSerializableMap map =  new ListenSerializableMap();
                map.setTitle(getTitleText());
                map.setSzList(szList);
                map.setSzInfoResponseMap(szInfoResponseMap);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ListenActivity.KEWEN_DATA, map);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        wuzitiTypeList.add("楷书");
        wuzitiTypeList.add("行书");
        wuzitiTypeList.add("草书");
        wuzitiTypeList.add("隶书");
        wuzitiTypeList.add("篆书");

        wutiTypeRecycler = (RecyclerView)findViewById(R.id.wutiTypeRecycler);
        wutiziAdapter = new WutiziTypeAdapter(this, wuzitiTypeList);
        wutiziAdapter.setOnItemClickListener(new WutiziTypeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                wutiziAdapter.setSelected(position);
                wutiziAdapter.notifyDataSetChanged();
                if (wutiziInfoAdapter != null){
                    wutiziInfoAdapter.setSelected(position);
                    wutiziInfoAdapter.notifyDataSetChanged();
                }
            }
        });

        wutiTypeRecycler.setLayoutManager(new GridLayoutManager(this, wuzitiTypeList.size()));
        wutiTypeRecycler.setHasFixedSize(true);
        wutiTypeRecycler.setAdapter(wutiziAdapter);

        initWutiInfo();

        initVideo();
    }

    private int wutiAdapterNowPos = -1;

    private void initWutiInfo(){
        wutiInfoRecycler = (RecyclerView)findViewById(R.id.wutiInfoRecycler);
        wutiziInfoAdapter = new WutiziInfoAdapter(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        wutiInfoRecycler.setLayoutManager(linearLayoutManager1);
        wutiInfoRecycler.setHasFixedSize(true);
        wutiInfoRecycler.setAdapter(wutiziInfoAdapter);

        wutizi_left_button =  (ImageButton)findViewById(R.id.wutizi_left_button);
        wutizi_right_button =  (ImageButton)findViewById(R.id.wutizi_right_button);


        wutiInfoRecycler.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
                LinearLayoutManager l = (LinearLayoutManager)recyclerView.getLayoutManager();
                wutiAdapterNowPos = l.findFirstVisibleItemPosition();
            }
        });

        wutizi_left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wutiAdapterNowPos <= 0){
                    showErrorMsg("不能继续向左了");
                }else {
                    wutiInfoRecycler.scrollToPosition(wutiAdapterNowPos-1);
                }
            }
        });

        wutizi_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wutiAdapterNowPos >= wutiziInfoAdapter.getItemCount()-1){
                    showErrorMsg("不能继续向右了");
                }else {
                    wutiInfoRecycler.scrollToPosition(wutiAdapterNowPos+1);
                }
            }
        });

    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return R.drawable.search_normal;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    private void loadSZInfo(final String text){
        GetSZInfoRequest request = new GetSZInfoRequest();
        request.setSz(text);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetSZInfoResponse szInfoResponse = gson.fromJson(response, GetSZInfoResponse.class);
                if (szInfoResponse != null && szInfoResponse.getHandleResult().equals("OK")){
                    szInfoResponseMap.put(text, szInfoResponse);
                    currentSzInfo = szInfoResponse;
                    updateSzRelatedUI(szInfoResponse);
                }else {
                    showErrorMsg("获取生字信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    private void updateSzRelatedUI(GetSZInfoResponse szInfoResponse){
        ((TextView)findViewById(R.id.pinyin_content)).setText(szInfoResponse.getPinyin());
        ((TextView)findViewById(R.id.sz_content)).setText(szInfoResponse.getWord());
        ((TextView)findViewById(R.id.bushou_content)).setText(szInfoResponse.getBushou()+"部");
        ((TextView)findViewById(R.id.zxjg_content)).setText(szInfoResponse.getZxjg());
        ((TextView)findViewById(R.id.zuci_content)).setText(szInfoResponse.getZuci());

        refreshWutizi(szInfoResponse);
        refreshVideoPlay(szInfoResponse);
    }

    private void refreshWutizi(GetSZInfoResponse szInfoResponse){
        wutiziInfoAdapter.setSzInfo(szInfoResponse);
        wutiziInfoAdapter.setSelected(wutiziAdapter.getSelected());
        wutiziInfoAdapter.notifyDataSetChanged();
    }

    private void refreshVideoPlay(GetSZInfoResponse szInfoResponse){
        //play media
        if (speakTypeValue == 0 && originSpeakTypeValue == SpeakType.SWJZ.getValue()){
            String url = szInfoResponse.getSwjz_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == 0 && originSpeakTypeValue == SpeakType.SWJZ_ZY.getValue()){
            String url = szInfoResponse.getSwjz_zy_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == 0 && originSpeakTypeValue == SpeakType.SWJZ_ZXYB.getValue()){
            String url = szInfoResponse.getSwjz_zxyb_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == 0 && originSpeakTypeValue == SpeakType.SWJZ_XXSY.getValue()){
            String url = szInfoResponse.getSwjz_xxsy_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == 0 && originSpeakTypeValue == SpeakType.SWJZ_GXGS.getValue()){
            String url = szInfoResponse.getSwjz_gs_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.BISHUN.getValue()){
            String url = szInfoResponse.getBishun_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.BANSHU.getValue()){
            String url = szInfoResponse.getBanshu_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.YINGBI.getValue()){
            String url = szInfoResponse.getYingbi_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }else if (speakTypeValue == SpeakType.MAOBI.getValue()){
            String url = szInfoResponse.getMaobi_url()+szInfoResponse.getWord()+VIDEO_SUFFIX;
            play(url);
        }
    }

    private void play(String url){
        LogUtil.d(TAG," play url = "+url);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
        mVideoView.requestFocus();

        mMediaController.setTitle(kewenTitle);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.speakType){
            mAdapter.setSelected(position);
            updateSpeakType(position);
            mAdapter.notifyDataSetChanged();
            if (currentSzInfo != null) {
                refreshVideoPlay(currentSzInfo);
            }
        }else if (view.getId() == R.id.speakSz){
            switchSz(position);
        }
    }

    @Override
    protected void onClickActionBarImageButton(){
        startSearchActivity(1);
    }

    private void startSearchActivity(int type){
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.SEARCH_TYPE,"sz");
        startActivity(intent);
    }

    private void updateSpeakType(int position){
        speakTypeValue = position;
    }

    private void switchSz(int position){
        mSZAdapter.setSelected(position);
        mSZAdapter.notifyDataSetChanged();
        SharedPrefUtil.saveSZPosition(getApplicationContext(), position);
        if (szInfoResponseMap.containsKey(szList.get(position))){
            updateSzRelatedUI(szInfoResponseMap.get(szList.get(position)));
            currentSzInfo = szInfoResponseMap.get(szList.get(position));
        }else {
            loadSZInfo(szList.get(position));
        }
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
                SpeakCNSzActivity.this.isFullscreen = isFullscreen;
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
        if (SpeakCNSzActivity.this.isFullscreen) {
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
