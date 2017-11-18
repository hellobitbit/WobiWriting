package com.wobi.android.wobiwriting.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.ClassCourseDirectoryAdapter;
import com.wobi.android.wobiwriting.home.adapters.ClassicDirectoryAdapter;
import com.wobi.android.wobiwriting.home.message.DiZiGuiRequest;
import com.wobi.android.wobiwriting.home.message.DiZiGuiResponse;
import com.wobi.android.wobiwriting.home.message.SanZiJingRequest;
import com.wobi.android.wobiwriting.home.message.SanZiJingResponse;
import com.wobi.android.wobiwriting.home.model.CNClassicCourse;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.HomeItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CNClassicActivity extends BaseVideoActivity {
    private static final String TAG = "CNClassicActivity";
    private List<CNClassicCourse> mDirectories =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra(HomeItemView.SUB_TYPE);
        initDirectory();
        if (HomeItemView.SUB_TYPE_1.equals(type)){
            loadDiZiGui();
            adapter.setSelected(0);
        }else if (HomeItemView.SUB_TYPE_2.equals(type)){
            loadSanZiJing();
            adapter.setSelected(1);
        }
        adapter.notifyDataSetChanged();
    }

    private void initData(){
        mTitles.add(getResources().getString(R.string.home_item_classic_dizigui));
        mTitles.add(getResources().getString(R.string.home_item_classic_three));
//        mTitles.add(getResources().getString(R.string.home_item_classic_daode));
//        mTitles.add(getResources().getString(R.string.home_item_classic_ancient_poetry));
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.home_item_classic;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    @Override
    protected void onClickActionBarImageButton(){
        startSearchActivity(CN_CLASSIC);
    }

    @Override
    public void onItemClick(View view, int position) {
        adapter.setSelected(position);
        adapter.notifyDataSetChanged();
        if (position == 0){
            loadDiZiGui();
        }else if (position == 1){
            loadSanZiJing();
        }
    }

    protected void initDirectory() {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_directory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 0, 9));
        mAdapter = new ClassicDirectoryAdapter(getApplicationContext(),mDirectories);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ClassCourseDirectoryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.d(TAG," position == "+position);
                String userInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
                UserGetInfoResponse userObj = gson.fromJson(userInfo, UserGetInfoResponse.class);
                if (TextUtils.isEmpty(userInfo)){
                    if (position == 0){
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                        play(mDirectories.get(position).getJieduUrl(),
                                mDirectories.get(position).getCourseName());
                    }else {
                        checkLogin();
                    }
                }else {
                    if (userObj.getIs_vip() == 1 || (userObj.getIs_vip() == 0
                            && (position == 0 || position == 1 || position ==2 ))){
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                        play(mDirectories.get(position).getJieduUrl(),
                                mDirectories.get(position).getCourseName());
                    }else {
                        checkVip();
                    }

                }

            }
        });
    }

    private void loadDiZiGui(){
        DiZiGuiRequest request = new DiZiGuiRequest();
        String jsonBody = request.jsonToString();
        sendRequest(jsonBody, ClassicType.DiZiGui);

    }

    private void loadSanZiJing(){
        SanZiJingRequest request = new SanZiJingRequest();
        String jsonBody = request.jsonToString();
        sendRequest(jsonBody, ClassicType.SanZiJing);
    }

    private void sendRequest(String body, final ClassicType type){
        NetDataManager.getInstance().getMessageSender().sendEvent(body, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                if (type == ClassicType.SanZiJing){
                    SanZiJingResponse sanZiJingResponse = gson.fromJson(response, SanZiJingResponse.class);
                    if (!sanZiJingResponse.getSzjList().isEmpty()){
                        mDirectories.clear();
                        mDirectories.addAll(sanZiJingResponse.getSzjList());
                    }
                }else if (type == ClassicType.DiZiGui){
                    DiZiGuiResponse diZiGuiResponse = gson.fromJson(response, DiZiGuiResponse.class);
                    if (!diZiGuiResponse.getDzjList().isEmpty()){
                        mDirectories.clear();
                        mDirectories.addAll(diZiGuiResponse.getDzjList());
                    }
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }

    private enum ClassicType{
        DiZiGui,
        SanZiJing
    }
}
