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
import com.wobi.android.wobiwriting.moments.adapters.PersonalMomentsAdapter;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesRequest;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesResponse;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchJoinedCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.SearchOwnedCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.moments.model.MomentData;
import com.wobi.android.wobiwriting.moments.model.Province;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoRequest;
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
    private Map<Integer, CommunityInfo> communityIds = new HashMap<>();

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
                startActivityForResult(intent, NewMomentActivity.REQUEST_CODE);
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
                Intent intent = new Intent(getApplicationContext(), MomentIntroduceActivity.class);
                MomentData momentData = new MomentData();
                momentData.setCommunityInfo(communityInfos.get(position));
                momentData.setProvinceMap(provinceMap);
                intent.putExtra(MomentIntroduceActivity.MOMENT_DATA,momentData);
                startActivityForResult(intent, MomentIntroduceActivity.REQUEST_CODE);
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
                showErrorMsg(errorMessage);
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
                showErrorMsg(errorMessage);
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
                for (CommunityInfo communityInfo: searchResponse.getCommunityList()){
                    if (!communityIds.containsKey(communityInfo.getId())){
                        communityIds.put(communityInfo.getId(), communityInfo);
                        communityInfos.add(communityInfo);
                    }
                }
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
                showErrorMsg(errorMessage);
            }
        });
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == NewMomentActivity.REQUEST_CODE
                && resultCode == NewMomentActivity.RESULT_CODE_SUCCESS){
            updateUserInfo();
            searchOwnedCommunity();
        }else if (requestCode == MomentIntroduceActivity.REQUEST_CODE
                && resultCode == MomentIntroduceActivity.RESULT_CODE_SUCCESS){
            int community_id = data.getIntExtra(MomentIntroduceActivity.RESULT_COMMUNITY_ID, -1);
            if (community_id != -1) {
                updateUserInfo();
                CommunityInfo communityInfo = communityIds.get(community_id);
                communityInfos.remove(communityInfo);
                momentsAdapter.notifyDataSetChanged();
            }

            CommunityInfo info = (CommunityInfo) data.getSerializableExtra("data");
            if (info != null){
                CommunityInfo target = communityIds.get(info.getId());
                if (communityInfos.contains(target)){
                    target.setCommunity_name(info.getCommunity_name());
                    target.setAddress(info.getAddress());
                    target.setIs_auth(info.getIs_auth());
                    target.setCity_code(info.getCity_code());
                    target.setSummary(info.getSummary());
                }
                momentsAdapter.notifyDataSetChanged();
            }
        }

    }

    private void updateUserInfo(){
        UserGetInfoRequest request = new UserGetInfoRequest();
        request.setUserId(userInfo.getUserId());
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                UserGetInfoResponse userGetInfoResponse = gson.fromJson(response, UserGetInfoResponse.class);
                if (userGetInfoResponse != null && userGetInfoResponse.getHandleResult().equals("OK")){
                    SharedPrefUtil.saveLoginInfo(getApplicationContext(),response);
                    userInfo = userGetInfoResponse;
                }else {
                    showErrorMsg("用户信息更新失败 "+ userGetInfoResponse.getHandleResult());
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }
}
