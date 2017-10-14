package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.CNClassicActivity;
import com.wobi.android.wobiwriting.home.CalligraphyClassActivity;
import com.wobi.android.wobiwriting.home.KewenDirectoryActivity;
import com.wobi.android.wobiwriting.home.SpeakCNActivity;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.home.adapters.CustomSpinnerAdapter;
import com.wobi.android.wobiwriting.home.message.GetGradeRequest;
import com.wobi.android.wobiwriting.home.message.GetGradeResponse;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.model.Grade;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.views.HomeItemView;
import com.wobi.android.wobiwriting.views.SpinnerPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class HomeFragment extends Fragment implements View.OnClickListener,
        AbstractSpinnerAdapter.IOnItemSelectListener{
    private static final String TAG = "HomeFragment";
    private Gson gson = new Gson();
    private TextView textView;
    private SpinnerPopWindow mSpinnerPopWindow;
    private CustomSpinnerAdapter mAdapter;
    private int mSelected = -1;

    private List<Grade> mGradeList = new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_frag_layout, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadGradeInfo();
        loadJCList();

        mAdapter = new CustomSpinnerAdapter(getActivity());
        mAdapter.refreshData(mGradeList, 0);

        mSpinnerPopWindow = new SpinnerPopWindow(getActivity());
        mSpinnerPopWindow.setAdapter(mAdapter);
        mSpinnerPopWindow.setItemListener(this);

        mSpinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                textView.setBackgroundResource(R.drawable.home_grade_title_default_shape_corner);
            }
        });
    }

    private void initView(View view){
        HomeItemView speckCN = (HomeItemView)view.findViewById(R.id.speak_chinese);
        HomeItemView cnClassic = (HomeItemView)view.findViewById(R.id.chinese_classic);
        HomeItemView calligraghyClass = (HomeItemView)view.findViewById(R.id.calligraghy_class);
        textView = (TextView)view.findViewById(R.id.dropdown);
        speckCN.setOnClickListener(this);
        cnClassic.setOnClickListener(this);
        calligraghyClass.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speak_chinese:
                Intent kewenDirIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
                getActivity().startActivity(kewenDirIntent);
                break;
            case R.id.calligraghy_class:
                Intent classIntent = new Intent(getActivity(), CalligraphyClassActivity.class);
                getActivity().startActivity(classIntent);
                break;
            case R.id.chinese_classic:
                Intent cnClassicIntent = new Intent(getActivity(), CNClassicActivity.class);
                getActivity().startActivity(cnClassicIntent);
                break;
            case R.id.dropdown:
                showSpinWindow();

        }
    }

    private void showSpinWindow(){
        mSpinnerPopWindow.setWidth(textView.getWidth());
        mSpinnerPopWindow.showAsDropDown(textView);
        textView.setBackgroundResource(R.drawable.home_grade_title_selected_shape_corner);
    }

    @Override
    public void onItemClick(int pos) {
        mSelected = pos;
        textView.setText(mGradeList.get(pos).getGradeName());
    }

    private void loadGradeInfo(){

        GetGradeRequest request = new GetGradeRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetGradeResponse getGradeResponse = gson.fromJson(response, GetGradeResponse.class);
                mGradeList.clear();
                mGradeList.addAll(getGradeResponse.getGradeList());
                mAdapter.refreshData(mGradeList, 0);
                mSelected = 0;
                textView.setText(mGradeList.get(0).getGradeName());
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }

    private void loadJCList(){
        GetJCListRequest request = new GetJCListRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetJCListResponse getJCListResponse = gson.fromJson(response, GetJCListResponse.class);
                mJCList.clear();
                mJCList.addAll(getJCListResponse.getJcList());

            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }
}
