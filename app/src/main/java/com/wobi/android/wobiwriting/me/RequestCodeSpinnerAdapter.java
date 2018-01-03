package com.wobi.android.wobiwriting.me;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;

/**
 * Created by wangyingren on 2017/12/25.
 */

public class RequestCodeSpinnerAdapter extends AbstractSpinnerAdapter<CommunityInfo> {

    public RequestCodeSpinnerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.request_code_spiner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.moment_name);
            viewHolder.first_text = (TextView) convertView.findViewById(R.id.sequence_num);
            viewHolder.last_text = (TextView) convertView.findViewById(R.id.request_code);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        CommunityInfo item = getItem(pos);
        viewHolder.mTextView.setText(item.getCommunity_name());
        viewHolder.first_text.setText(" "+(pos+1)+". ");
//        viewHolder.last_text.setText(item.getCity_code());
        viewHolder.last_text.setText(item.getRequest_code());

        return convertView;
    }
}
