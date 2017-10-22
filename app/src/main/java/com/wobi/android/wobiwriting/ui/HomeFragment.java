package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.CNClassicActivity;
import com.wobi.android.wobiwriting.home.CalligraphyClassActivity;
import com.wobi.android.wobiwriting.home.KewenDirectoryActivity;
import com.wobi.android.wobiwriting.home.SpeakCNActivity;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.home.adapters.BannerViewpagerAdapter;
import com.wobi.android.wobiwriting.home.adapters.CustomSpinnerAdapter;
import com.wobi.android.wobiwriting.home.message.GetGradeRequest;
import com.wobi.android.wobiwriting.home.message.GetGradeResponse;
import com.wobi.android.wobiwriting.home.model.Grade;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.views.HomeItemView;
import com.wobi.android.wobiwriting.views.SpinnerPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,
        AbstractSpinnerAdapter.IOnItemSelectListener{
    private static final String TAG = "HomeFragment";
    private TextView textView;
    private SpinnerPopWindow mSpinnerPopWindow;
    private CustomSpinnerAdapter mAdapter;
    private int mSelected = -1;

    private List<Grade> mGradeList = new ArrayList<>();
    private ViewPager banner_viewpager;
    private HomeItemView cnClassic;
    private HomeItemView calligraghyClass;
    private HomeItemView chinese_writing;
    private HomeItemView speckCN;

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
        speckCN = (HomeItemView)view.findViewById(R.id.speak_chinese);
        cnClassic = (HomeItemView)view.findViewById(R.id.chinese_classic);
        chinese_writing = (HomeItemView)view.findViewById(R.id.chinese_writing);
        Intent cnClassicIntent = new Intent(getActivity(), CNClassicActivity.class);
        cnClassic.setMainAndSub1Intent(cnClassicIntent,true);
        cnClassic.setSub2Intent(cnClassicIntent, true);
        cnClassic.setSub3Intent(cnClassicIntent,false);
        cnClassic.setSub4Intent(cnClassicIntent,false);

        calligraghyClass = (HomeItemView)view.findViewById(R.id.calligraghy_class);
        textView = (TextView)view.findViewById(R.id.dropdown);

        speckCN.setOnClickListener(this);
        textView.setOnClickListener(this);

        banner_viewpager = (ViewPager) view.findViewById(R.id.banner_viewpager);
        banner_viewpager.setAdapter(new BannerViewpagerAdapter(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speak_chinese:
                if (mGradeList.size() > 0) {
                    Intent intent = new Intent(getActivity(), SpeakCNActivity.class);
                    intent.putExtra(SpeakCNActivity.GRADE_ID,
                            mGradeList.get(mSelected).getGradeId());
                    intent.putExtra(SpeakCNActivity.SPEAK_TYPE,
                            SpeakCNActivity.SpeakType.SWJZ.getValue());
                    getActivity().startActivity(intent);
                }else {
                    showNetWorkException();
                }
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
        refreshGradeInfo(pos);
    }

    private void refreshGradeInfo(int position){
        mSelected = position;
        textView.setText(mGradeList.get(position).getGradeName());

        Intent classIntent = new Intent(getActivity(), CalligraphyClassActivity.class);
        classIntent.putExtra(CalligraphyClassActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        calligraghyClass.setMainAndSub1Intent(classIntent,true);
        calligraghyClass.setSub3Intent(classIntent,true);

        Intent bishunIntent = new Intent(getActivity(), SpeakCNActivity.class);
        bishunIntent.putExtra(SpeakCNActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        bishunIntent.putExtra(SpeakCNActivity.SPEAK_TYPE,
                SpeakCNActivity.SpeakType.BISHUN.getValue());
        chinese_writing.setMainAndSub1Intent(bishunIntent,true);

        Intent banshuIntent = new Intent(getActivity(), SpeakCNActivity.class);
        banshuIntent.putExtra(SpeakCNActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        banshuIntent.putExtra(SpeakCNActivity.SPEAK_TYPE,
                SpeakCNActivity.SpeakType.BANSHU.getValue());
        chinese_writing.setSub2Intent(banshuIntent,true);

        Intent yingbiIntent = new Intent(getActivity(), SpeakCNActivity.class);
        yingbiIntent.putExtra(SpeakCNActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        yingbiIntent.putExtra(SpeakCNActivity.SPEAK_TYPE,
                SpeakCNActivity.SpeakType.YINGBI.getValue());
        chinese_writing.setSub3Intent(yingbiIntent,true);

        Intent maobiIntent = new Intent(getActivity(), SpeakCNActivity.class);
        maobiIntent.putExtra(SpeakCNActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        maobiIntent.putExtra(SpeakCNActivity.SPEAK_TYPE,
                SpeakCNActivity.SpeakType.MAOBI.getValue());
        chinese_writing.setSub4Intent(maobiIntent,true);
    }

    private void loadGradeInfo(){
        GetGradeRequest request = new GetGradeRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                try {
                    GetGradeResponse getGradeResponse = gson.fromJson(response, GetGradeResponse.class);
                    if (getGradeResponse != null && getGradeResponse.getHandleResult().equals("OK")) {
                        if (getGradeResponse.getGradeList() != null){
                            mGradeList.clear();
                            mGradeList.addAll(getGradeResponse.getGradeList());
                            refreshGradeInfo(0);
                        }else {
                            showErrorMsg("没有年级信息");
                        }
                    }else {
                        showErrorMsg("获取年级信息失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    showErrorMsg("获取年级信息异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }
}
