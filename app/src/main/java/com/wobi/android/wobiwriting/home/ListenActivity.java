package com.wobi.android.wobiwriting.home;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.ListenSZAdapter;
import com.wobi.android.wobiwriting.home.message.GetSZInfoRequest;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.home.model.ListenSerializableMap;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/21.
 */

public class ListenActivity extends ActionBarActivity
        implements View.OnClickListener{

    public static final String KEWEN_DATA = "kewen_data";
    private static final String TAG = "ListenActivity";
    private ListenSerializableMap kewenDataMap;
    private ListenSZAdapter listenSZAdapter;
    private HashMap<String, GetSZInfoResponse> szInfoResponseHashMap;

    private final List<GetSZInfoResponse> szInfoResponses = new ArrayList<>();
    private RelativeLayout listen_sz_control_layout;
    private LinearLayout play_control;
    private LinearLayout play_sequence_control;
    private LinearLayout play_num_control;
    private TextView sz_num;

    private PlayType playType = PlayType.Stop;
    private SequenceType sequenceType = SequenceType.Order;
    private PlayNumber playNumber = PlayNumber.Once;
    private TextView play_control_label;
    private TextView play_sequence_control_text;
    private TextView play_num_control_text;
    private ImageView play_control_icon;
    private ImageView play_sequence_control_icon;
    private ImageView play_num_control_icon;
    private MediaPlayer mPlayer;

    private String url = "";
    private int count = 0;

    private int currentPosition = 0;

    private List<String> playLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_writing_layout);
        Bundle bundle = getIntent().getExtras();
        kewenDataMap = (ListenSerializableMap) bundle.get(KEWEN_DATA);
        initViews();
        setCustomActionBar();
        updateImageResource(R.drawable.listen_writing_exit);
        if (kewenDataMap != null) {
            szInfoResponseHashMap =  kewenDataMap.getSzInfoResponseMap();
            updateTitleText(kewenDataMap.getTitle());
            initData();
            displayControlLayout();
            sz_num.setText(" ( "+kewenDataMap.getSzList().size()+"字 ）");
            updatePlayControlUI();
            updatePlaySequenceUI();
            updatePlayNumberUI();
            initMediaPlayer();
        }
    }

    private void initData(){
        for (String sz: kewenDataMap.getSzList()){
            if (szInfoResponseHashMap.containsKey(sz)){
                szInfoResponses.add(szInfoResponseHashMap.get(sz));
            }else {
                loadSZInfo(sz);
            }
        }
    }

    private void initViews() {
        RecyclerView listenSZListRecycler = (RecyclerView)findViewById(R.id.listenSZListRecycler);
        listenSZAdapter = new ListenSZAdapter(this, szInfoResponses);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listenSZListRecycler.setLayoutManager(layoutManager);
        listenSZListRecycler.setHasFixedSize(true);
        listenSZListRecycler.setAdapter(listenSZAdapter);

        listen_sz_control_layout = (RelativeLayout)findViewById(R.id.listen_sz_control_layout);
        play_control = (LinearLayout)findViewById(R.id.play_control);
        play_sequence_control = (LinearLayout)findViewById(R.id.play_sequence_control);
        play_num_control = (LinearLayout)findViewById(R.id.play_num_control);
        play_control.setOnClickListener(this);
        play_sequence_control.setOnClickListener(this);
        play_num_control.setOnClickListener(this);

        sz_num = (TextView)findViewById(R.id.sz_num);

        play_control_label = (TextView)findViewById(R.id.play_control_label);
        play_sequence_control_text = (TextView)findViewById(R.id.play_sequence_control_text);
        play_num_control_text = (TextView)findViewById(R.id.play_num_control_text);

        play_control_icon = (ImageView)findViewById(R.id.play_control_icon);
        play_sequence_control_icon = (ImageView)findViewById(R.id.play_sequence_control_icon);
        play_num_control_icon = (ImageView)findViewById(R.id.play_num_control_icon);
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return 0;
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
                    szInfoResponseHashMap.put(text, szInfoResponse);
                    updateSZDisplay(text, szInfoResponse);
                }else {
                    showErrorMsg("获取生字信息数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void updateSZDisplay(String text, GetSZInfoResponse szInfoResponse){
        int position = kewenDataMap.getSzList().indexOf(text);
        if ((position)> szInfoResponses.size()-1){
            szInfoResponses.add(szInfoResponse);
        }else {
            szInfoResponses.add(position,szInfoResponse);
        }
        listenSZAdapter.notifyDataSetChanged();
    }

    private void displayControlLayout(){
        if (kewenDataMap.getSzList().size() > 0){
            listen_sz_control_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_control:
                if (playType == PlayType.Play){
                    playType =PlayType.Stop;
                }else {
                    playType =PlayType.Play;
                }
                updatePlayControlUI();
                break;
            case R.id.play_sequence_control:
                if (playType == PlayType.Stop){
                    if (sequenceType == SequenceType.Order){
                        sequenceType = SequenceType.Random;
                    }else {
                        sequenceType = SequenceType.Order;
                    }
                }else {
                    showErrorMsg("操作失败，请先停止播放");
                }
                updatePlaySequenceUI();
                break;
            case R.id.play_num_control:
                if (playType == PlayType.Stop){
                    if (playNumber == PlayNumber.Once){
                        playNumber = PlayNumber.Twice;
                    }else if (playNumber == PlayNumber.Twice){
                        playNumber = PlayNumber.Three;
                    }else if (playNumber == PlayNumber.Three){
                        playNumber = PlayNumber.Once;
                    }
                }else {
                    showErrorMsg("操作失败，请先停止播放");
                }
                updatePlayNumberUI();
                break;
        }
    }

    private void updatePlayControlUI(){
        if (playType == PlayType.Play){
            //must modify the icon
            play_control_icon.setImageResource(R.drawable.media_stop);
            play_control_label.setText("停止");
            startPlay();
        }else if (playType == PlayType.Stop){
            play_control_icon.setImageResource(R.drawable.media_start);
            play_control_label.setText("播放");
            stopPlay();
        }
    }

    private void updatePlaySequenceUI(){
        if (sequenceType == SequenceType.Order){
            play_sequence_control_icon.setImageResource(R.drawable.listen_shunxu_icon);
            play_sequence_control_text.setText("顺序播放");
        }else if (sequenceType == SequenceType.Random){
            play_sequence_control_icon.setImageResource(R.drawable.listen_random_icon);
            play_sequence_control_text.setText("随机播放");
        }
    }

    private void updatePlayNumberUI(){
        if (playNumber == PlayNumber.Once){
            play_num_control_icon.setImageResource(R.drawable.play_num_control_icon1);
            play_num_control_text.setText("播放一遍");
        }else if (playNumber == PlayNumber.Twice){
            play_num_control_icon.setImageResource(R.drawable.play_num_control_icon2);
            play_num_control_text.setText("播放两遍");
        }else if (playNumber == PlayNumber.Three){
            play_num_control_icon.setImageResource(R.drawable.play_num_control_icon3);
            play_num_control_text.setText("播放三遍");
        }
    }

    private enum PlayType{
        Play,
        Stop
    }

    private enum SequenceType{
        Order,
        Random
    }

    private enum PlayNumber{
        Once,
        Twice,
        Three
    }

    private void initMediaPlayer(){
        mPlayer = new MediaPlayer() ;
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.d(TAG," mediaplayer onCompletion");
                if (currentPosition < playLists.size()-1){
                    currentPosition++;
                    playStreaming(playLists.get(currentPosition));
                }else {
                    currentPosition = 0;
                    playLists.clear();
                    play_control.performClick();
                }
            }
        });
    }

    private void startPlay(){
        for (GetSZInfoResponse szInfo: szInfoResponses){
            if (!TextUtils.isEmpty(szInfo.getZuci1())){
                String url = szInfo.getZuci_url()+szInfo.getZuci1()+".mp3";
                playLists.add(url);
            }

            if (!TextUtils.isEmpty(szInfo.getZuci2())){
                String url = szInfo.getZuci_url()+szInfo.getZuci2()+".mp3";
                playLists.add(url);
            }

            if (!TextUtils.isEmpty(szInfo.getZuci3())){
                String url = szInfo.getZuci_url()+szInfo.getZuci3()+".mp3";
                playLists.add(url);
            }
        }
        if (playLists.size() > 0){
            playStreaming(playLists.get(currentPosition));
        }
    }

    private void stopPlay(){
        count = 0;
        url = "";
    }

    private void playStreaming(String url){
        LogUtil.d(TAG, " url = "+url);
        //从网路加载音乐
        try {
            mPlayer.setDataSource(url) ;
            //需使用异步缓冲
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }
}
