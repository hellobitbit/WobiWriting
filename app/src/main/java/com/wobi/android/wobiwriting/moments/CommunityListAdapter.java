package com.wobi.android.wobiwriting.moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/11.
 */

public class CommunityListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<CommunityInfo> communityInfos;

    public CommunityListAdapter(Context context, List<CommunityInfo> communityInfos){
        mContext = context;
        this.communityInfos = communityInfos;
    }
    @Override
    public int getCount() {
        return communityInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.moment_item_layout,null);
        }
        CommunityInfo info = communityInfos.get(position);
        ImageView moment_icon = (ImageView)convertView.findViewById(R.id.moment_icon);
        TextView moment_name = (TextView)convertView.findViewById(R.id.moment_name);
        TextView moment_code = (TextView)convertView.findViewById(R.id.moment_code);
        TextView moment_time = (TextView)convertView.findViewById(R.id.moment_time);
        TextView moment_summary = (TextView)convertView.findViewById(R.id.moment_summary);
        moment_name.setText(info.getCommunity_name());
        moment_code.setText("邀请码："+info.getRequest_code());
        moment_summary.setText(info.getSummary());
        moment_time.setText(info.getCreate_time());
        return convertView;
    }
}
