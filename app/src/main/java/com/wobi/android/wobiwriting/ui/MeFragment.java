package com.wobi.android.wobiwriting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wobi.android.wobiwriting.R;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.me_frag_layout, null);
        return view;
    }
}
