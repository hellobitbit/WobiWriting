package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.SpeakCNActivity;
import com.wobi.android.wobiwriting.views.HomeItemView;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_frag_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        HomeItemView speckCN = (HomeItemView)view.findViewById(R.id.speak_chinese);
        speckCN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speak_chinese:
                Intent speakCNIntent = new Intent(getActivity(), SpeakCNActivity.class);
                getActivity().startActivity(speakCNIntent);
                break;
        }
    }
}
