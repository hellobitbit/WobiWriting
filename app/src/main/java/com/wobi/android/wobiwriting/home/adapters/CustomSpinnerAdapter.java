package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.model.Grade;

/**
 * Created by wangyingren on 2017/9/24.
 */

public class CustomSpinnerAdapter extends AbstractSpinnerAdapter<Grade> {

    public CustomSpinnerAdapter(Context context) {
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


        Grade item = getItem(pos);
        viewHolder.mTextView.setText(item.getGradeName());

        return convertView;
    }
}
