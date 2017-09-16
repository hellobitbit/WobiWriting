package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.me.FeedbackActivity;
import com.wobi.android.wobiwriting.me.MyFollowActivity;
import com.wobi.android.wobiwriting.me.MyMomentActivity;
import com.wobi.android.wobiwriting.me.MyWodouActivity;
import com.wobi.android.wobiwriting.me.MyInformationActivity;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class MeFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.me_frag_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        FrameLayout userInfo = (FrameLayout)view.findViewById(R.id.user_info);

        RelativeLayout shareApp = (RelativeLayout)view.findViewById(R.id.share_app);
        RelativeLayout userFeedback = (RelativeLayout)view.findViewById(R.id.user_feedback);
        RelativeLayout accountExit = (RelativeLayout)view.findViewById(R.id.account_exit);

        LinearLayout momentsNumLayout = (LinearLayout)view.findViewById(R.id.moments_num_layout);
        LinearLayout followNumLayout = (LinearLayout)view.findViewById(R.id.follow_num_layout);
        LinearLayout wodouNumLayout = (LinearLayout)view.findViewById(R.id.wodou_num_layout);

        shareApp.setOnClickListener(this);

        userFeedback.setOnClickListener(this);
        accountExit.setOnClickListener(this);
        userInfo.setOnClickListener(this);

        momentsNumLayout.setOnClickListener(this);
        followNumLayout.setOnClickListener(this);
        wodouNumLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_exit:
                break;
            case R.id.user_feedback:
                Intent feedback = new Intent(getActivity(), FeedbackActivity.class);
                getActivity().startActivity(feedback);
                break;
            case R.id.share_app:
                break;
            case R.id.user_info:
                Intent personalInfo = new Intent(getActivity(), MyInformationActivity.class);
                getActivity().startActivity(personalInfo);
                break;
            case R.id.moments_num_layout:
                Intent moments = new Intent(getActivity(), MyMomentActivity.class);
                getActivity().startActivity(moments);
                break;
            case R.id.follow_num_layout:
                Intent follow = new Intent(getActivity(), MyFollowActivity.class);
                getActivity().startActivity(follow);
                break;
            case R.id.wodou_num_layout:
                Intent wodou = new Intent(getActivity(), MyWodouActivity.class);
                getActivity().startActivity(wodou);
                break;
        }
    }
}
