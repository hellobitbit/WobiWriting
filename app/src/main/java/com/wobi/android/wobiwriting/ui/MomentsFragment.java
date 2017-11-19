package com.wobi.android.wobiwriting.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.moments.MomentDetailActivity;
import com.wobi.android.wobiwriting.moments.MomentsAdapter;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityByKeywordRequest;
import com.wobi.android.wobiwriting.moments.message.SearchCommunityResultResponse;
import com.wobi.android.wobiwriting.moments.message.SearchPopularCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MomentsFragment extends BaseFragment implements View.OnClickListener,
        MomentsAdapter.OnRecyclerViewItemClickListener {
    private static final String TAG = "MomentsFragment";
    private ImageView mSendMoment;
    private List<CommunityInfo> popularCommunityInfos = new ArrayList<>();
    private List<CommunityInfo> searchCommunityInfos = new ArrayList<>();
    private List<CommunityInfo> communityInfos = new ArrayList<>();
    private MomentsAdapter momentsAdapter;
    private DisplayType displayType = DisplayType.Popular;
    private EditText searchBar;

    private enum DisplayType{
        Popular,
        Search_Name,
        Search_Code
    }

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
        momentsAdapter.setOnItemClickListener(this);
        momentsRecycler.setAdapter(momentsAdapter);

        checkPopularCommunity();

        mSendMoment = (ImageView)view.findViewById(R.id.sendMoment);
        mSendMoment.setOnClickListener(this);

        searchBar = (EditText)view.findViewById(R.id.searchBar);

        (view.findViewById(R.id.cancelSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftware();
                searchBar.setText("");
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == 0 || actionId == 3) && event != null) {
                    //点击搜索要做的操作
                    startSearch();
                }
                return false;
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.i(TAG, searchBar.getText().toString());
                if (count == 0){
                    communityInfos.clear();
                    communityInfos.addAll(popularCommunityInfos);
                    momentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchBar.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
    }

    private void startSearch() {
        String key = searchBar.getText().toString();
        LogUtil.d(TAG, "search key = "+key);
//        searchCommunityListByName(key);
        searchCommunityListByKeyword(key);
        hideSoftware();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendMoment:
                boolean guestUser = SharedPrefUtil.getLoginInfo(getActivity()).isEmpty();
                if (!guestUser){
                    hideSoftware();
//                    Intent sendMomentIntent = new Intent(getActivity(), SendMomentActivity.class);
//                    getActivity().startActivity(sendMomentIntent);
                    showErrorMsg("该版本未有此功能，敬请期待");
                }else {
                    checkLogin();
                }
                break;
        }
    }

    private void checkPopularCommunity(){
        if (popularCommunityInfos.size() == 0){
            searchPopularCommunity();
        }
    }

    private void searchPopularCommunity(){
        SearchPopularCommunityRequest request = new SearchPopularCommunityRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                SearchCommunityResultResponse searchPopularCommunityResponse = gson.
                        fromJson(response,SearchCommunityResultResponse.class);
                if (searchPopularCommunityResponse != null
                        && searchPopularCommunityResponse.getHandleResult().equals("OK")){
                    if (searchPopularCommunityResponse.getCommunityList() == null
                            || searchPopularCommunityResponse.getCommunityList().size() == 0){
                        showErrorMsg("当前不存在圈子");
                    }else {
                        popularCommunityInfos.clear();
                        popularCommunityInfos.addAll(searchPopularCommunityResponse.getCommunityList());
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(TAG," onHiddenChanged hidden = "+hidden);
        if (!hidden){
            checkPopularCommunity();
        }
    }

    private void searchCommunityListByKeyword(String keyword){
        SearchCommunityByKeywordRequest request = new SearchCommunityByKeywordRequest();
        request.setKeyword(keyword);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                SearchCommunityResultResponse communityByCodeResponse = gson.fromJson(response,
                        SearchCommunityResultResponse.class);
                if (communityByCodeResponse != null && communityByCodeResponse.getHandleResult().equals("OK")){
                    if (communityByCodeResponse.getCommunityList() == null
                            || communityByCodeResponse.getCommunityList().size() == 0){
                        showErrorMsg("当前没有该圈子，请输入其他关键词");
                    }else {
                        searchCommunityInfos.clear();
                        searchCommunityInfos.addAll(communityByCodeResponse.getCommunityList());
                        communityInfos.clear();
                        communityInfos.addAll(communityByCodeResponse.getCommunityList());
                        momentsAdapter.notifyDataSetChanged();
                    }
                }else {
                    showErrorMsg("搜索异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        boolean guestUser = SharedPrefUtil.getLoginInfo(getActivity()).isEmpty();
        if (!guestUser ){
            Intent intent = new Intent(getActivity(), MomentDetailActivity.class);
            intent.putExtra(MomentDetailActivity.COMMUNITY_INFO,communityInfos.get(position));
            getActivity().startActivity(intent);
        }else {
            checkLogin();
        }
    }

    private void checkLogin(){
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage("登录后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
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

    private void hideSoftware(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    private static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }
}
