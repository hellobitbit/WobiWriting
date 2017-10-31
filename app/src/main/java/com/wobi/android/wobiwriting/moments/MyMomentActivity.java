package com.wobi.android.wobiwriting.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.moments.ModifyMomentActivity;
import com.wobi.android.wobiwriting.moments.NewMomentActivity;
import com.wobi.android.wobiwriting.moments.NewOrModifyMomentBaseActivity;
import com.wobi.android.wobiwriting.moments.adapters.PersonalMomentsAdapter;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesRequest;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesResponse;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchJoinedCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.SearchOwnedCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.moments.model.Province;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/9/14.
 */

public class MyMomentActivity extends ActionBarActivity {

    private static final String TAG = "MyMomentActivity";
    private UserGetInfoResponse userInfo;
    private PersonalMomentsAdapter momentsAdapter;

    private List<CommunityInfo> communityInfos = new ArrayList<>();

    private Map<String, String> provinceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        initViews();
        setCustomActionBar();
        initData();
    }

    private void initViews(){

        LinearLayout new_moment_layout = (LinearLayout)findViewById(R.id.new_moment_layout);
        new_moment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewMomentActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView momentsRecycler = (RecyclerView)findViewById(R.id.momentsRecycler);
        momentsAdapter = new PersonalMomentsAdapter(getApplicationContext(), communityInfos,
                userInfo.getUserId(), provinceMap);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        momentsRecycler.setLayoutManager(layoutManager);
        momentsRecycler.setHasFixedSize(true);
        momentsRecycler.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 0, 6));
        momentsAdapter.setOnItemClickListener(new PersonalMomentsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        momentsRecycler.setAdapter(momentsAdapter);
    }

    private void initData(){
        searchAllProvinces();
        searchOwnedCommunity();
        searchJoinedCommunity();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.activity_my_moment_title;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return -1;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return -1;
    }

    private void searchOwnedCommunity(){
        SearchOwnedCommunityRequest request = new SearchOwnedCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                updateCommunities(response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void searchJoinedCommunity(){
        SearchJoinedCommunityRequest request = new SearchJoinedCommunityRequest();
        request.setUser_id(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                updateCommunities(response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void updateCommunities(String response){
        LogUtil.d(TAG," response: "+response);
        SearchCommunityResultResponse searchResponse = gson.
                fromJson(response,SearchCommunityResultResponse.class);
        if (searchResponse != null
                && searchResponse.getHandleResult().equals("OK")){
            if (searchResponse.getCommunityList() == null
                    || searchResponse.getCommunityList().size() == 0){
//                showErrorMsg("当前不存在圈子");
            }else {
                communityInfos.clear();
                communityInfos.addAll(searchResponse.getCommunityList());
                momentsAdapter.notifyDataSetChanged();
            }
        }else {
            showErrorMsg("获取数据异常");
        }
    }

    private void searchAllProvinces(){
        GetAllProvincesRequest request = new GetAllProvincesRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetAllProvincesResponse getAllProvincesResponse = gson.fromJson(response,
                        GetAllProvincesResponse.class);
                if(getAllProvincesResponse != null &&
                        getAllProvincesResponse.getHandleResult().equals("OK")){
                    if (getAllProvincesResponse.getProvince_list() != null &&
                            getAllProvincesResponse.getProvince_list().size() > 0){
                        List<Province> provinces = getAllProvincesResponse.getProvince_list();
                        for (Province province: provinces){
                            provinceMap.put(province.getCity_code(),province.getProvince_name()
                                    .replace("省","").trim());
                        }

                        momentsAdapter.notifyDataSetChanged();
                    }
                }else {

                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }
}
