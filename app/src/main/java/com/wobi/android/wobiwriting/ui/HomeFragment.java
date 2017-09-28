package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.CNClassicActivity;
import com.wobi.android.wobiwriting.home.CalligraphyClassActivity;
import com.wobi.android.wobiwriting.home.SpeakCNActivity;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.home.adapters.CustomSpinnerAdapter;
import com.wobi.android.wobiwriting.views.HomeItemView;
import com.wobi.android.wobiwriting.views.SpinnerPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, AbstractSpinnerAdapter.IOnItemSelectListener{

    private TextView textView;
    private SpinnerPopWindow mSpinnerPopWindow;
    private CustomSpinnerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_frag_layout, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        List<String> nameList = new ArrayList<>();
        nameList.add("test1");
        nameList.add("test2");
        nameList.add("test3");
        nameList.add("test4");
        nameList.add("test5");
        nameList.add("test6");
        nameList.add("test7");

        mAdapter = new CustomSpinnerAdapter(getActivity());
        mAdapter.refreshData(nameList, 0);

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
                Intent speakCNIntent = new Intent(getActivity(), SpeakCNActivity.class);
                getActivity().startActivity(speakCNIntent);
                break;
            case R.id.chinese_classic:
                Intent cnClassicIntent = new Intent(getActivity(), CNClassicActivity.class);
                getActivity().startActivity(cnClassicIntent);
                break;
            case R.id.calligraghy_class:
                Intent classIntent = new Intent(getActivity(), CalligraphyClassActivity.class);
                getActivity().startActivity(classIntent);
                break;
            case R.id.dropdown:
                showSpinWindow();

        }
    }

    private void showSpinWindow(){
        Log.e("yingrenw", "showSpinWindow");
        mSpinnerPopWindow.setWidth(textView.getWidth());
        mSpinnerPopWindow.showAsDropDown(textView);
        textView.setBackgroundResource(R.drawable.home_grade_title_selected_shape_corner);
    }

    @Override
    public void onItemClick(int pos) {
        Log.e("yingrenw", "onItemClick pos = "+pos);
    }
}
