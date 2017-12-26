package com.wobi.android.wobiwriting.me;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;

import java.util.List;

/**
 * Created by wangyingren on 2017/12/25.
 */

public class RequestCodeSpinnerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener{

    private Context mContext;
    private ListView mListView;
    private AbstractSpinnerAdapter mAdapter;
    private AbstractSpinnerAdapter.IOnItemSelectListener mItemSelectListener;


    public RequestCodeSpinnerPopWindow(Context context) {
        super(context);

        mContext = context;
        init();
    }


    public void setItemListener(AbstractSpinnerAdapter.IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdapter(AbstractSpinnerAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.request_code_spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);


        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
    }


    public <T> void refreshData(List<T> list, int selIndex) {
        if (list != null && selIndex != -1) {
            if (mAdapter != null) {
                mAdapter.refreshData(list, selIndex);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(pos);
        }
    }
}
