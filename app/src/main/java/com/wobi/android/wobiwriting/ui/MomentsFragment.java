package com.wobi.android.wobiwriting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.moments.CommunityListAdapter;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MomentsFragment extends Fragment {

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.moments_frag_layout, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        mListView = (ListView) view.findViewById(R.id.momentsList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CommunityListAdapter adapter = new CommunityListAdapter(getActivity());
        mListView.setAdapter(adapter);
    }
}
