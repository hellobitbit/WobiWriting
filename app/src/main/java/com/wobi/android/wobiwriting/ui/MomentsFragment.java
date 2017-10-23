package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.home.adapters.ListenSZAdapter;
import com.wobi.android.wobiwriting.moments.CommunityListAdapter;
import com.wobi.android.wobiwriting.moments.MomentsAdapter;
import com.wobi.android.wobiwriting.moments.SendMomentActivity;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityByNameRequest;
import com.wobi.android.wobiwriting.moments.message.SearchPopularCommunityRequest;
import com.wobi.android.wobiwriting.moments.message.SearchPopularCommunityResponse;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MomentsFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "MomentsFragment";
    private ImageView mSendMoment;
    private List<CommunityInfo> communityInfos = new ArrayList<>();
    private MomentsAdapter momentsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.moments_frag_layout, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        RecyclerView momentsRecycler = (RecyclerView)view.findViewById(R.id.momentsRecycler);
        momentsAdapter = new MomentsAdapter(getActivity(), communityInfos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        momentsRecycler.setLayoutManager(layoutManager);
        momentsRecycler.setHasFixedSize(true);
        momentsRecycler.addItemDecoration(new SpaceItemDecoration(getActivity(), 0, 12));
        momentsRecycler.setAdapter(momentsAdapter);

        searchPopularCommunity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendMoment:
                Intent sendMomentIntent = new Intent(getActivity(), SendMomentActivity.class);
                getActivity().startActivity(sendMomentIntent);
                break;
        }
    }

    private void searchPopularCommunity(){
        SearchPopularCommunityRequest request = new SearchPopularCommunityRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                SearchPopularCommunityResponse searchPopularCommunityResponse = gson.
                        fromJson(response,SearchPopularCommunityResponse.class);
                if (searchPopularCommunityResponse != null
                        && searchPopularCommunityResponse.getHandleResult().equals("OK")){
                    if (searchPopularCommunityResponse.getCommunityList() == null
                            || searchPopularCommunityResponse.getCommunityList().size() == 0){
                        showErrorMsg("当前不存在圈子");
                    }else {
                        communityInfos.clear();
                        communityInfos.addAll(searchPopularCommunityResponse.getCommunityList());
                        momentsAdapter.notifyDataSetChanged();
                    }
                }else {
                    showErrorMsg("获取数据异常");
                }

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void searchCommunityListbyName(String name){
        SearchCommunityByNameRequest request = new SearchCommunityByNameRequest();
        request.setKeyword(name);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }
}
