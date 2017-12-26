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
            convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        CommunityInfo item = getItem(pos);
        viewHolder.mTextView.setText("test");

        return convertView;
    }
}
