package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.message.GetSCInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/12/2.
 */

public class MainSCDisplayAdapter extends RecyclerView.Adapter<MainSCDisplayAdapter.SZItemViewHolder> {

    private static final String TAG  = "MainSCDisplayAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<SC_SZ_Item> sc_sz_items = new ArrayList<>();

    public MainSCDisplayAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void updateInfo(GetSCInfoResponse data){
        sc_sz_items.clear();
        if (data != null && !TextUtils.isEmpty(data.getSc())){

            char[] chars = data.getSc().toCharArray();
            LogUtil.d(TAG, "getItemCount == "+chars.length);
            for (int i=0; i<chars.length; i++){
                if (i == 0){
                    SC_SZ_Item item = new SC_SZ_Item(""+chars[i], data.getPinyin1());
                    sc_sz_items.add(item);
                }
                if (i == 1){
                    SC_SZ_Item item = new SC_SZ_Item(""+chars[i], data.getPinyin2());
                    sc_sz_items.add(item);
                }
                if (i == 2){
                    SC_SZ_Item item = new SC_SZ_Item(""+chars[i], data.getPinyin3());
                    sc_sz_items.add(item);
                }
                if (i == 3){
                    SC_SZ_Item item = new SC_SZ_Item(""+chars[i], data.getPinyin4());
                    sc_sz_items.add(item);
                }
            }

        }
    }


    @Override
    public SZItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SZItemViewHolder(mInflater.inflate(R.layout.sc_sz_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SZItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return sc_sz_items.size();
    }

    public class SZItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sc_sz_pinyin;
        private TextView sc_sz;

        public SZItemViewHolder(View itemView) {
            super(itemView);
            sc_sz_pinyin = (TextView) itemView.findViewById(R.id.sc_sz_pinyin);
            sc_sz = (TextView) itemView.findViewById(R.id.sc_sz);

        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            sc_sz_pinyin.setText(sc_sz_items.get(position).getPinyin());
            sc_sz.setText(sc_sz_items.get(position).getSc_sz());
        }

        @Override
        public void onClick(View v) {
//            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }

    private class SC_SZ_Item{
        private String pinyin;
        private String sc_sz;

        public SC_SZ_Item(String sc_sz, String pinyin){
            this.pinyin = pinyin;
            this.sc_sz = sc_sz;
        }

        public String getPinyin(){
            return pinyin;
        }

        public String getSc_sz(){
            return sc_sz;
        }
    }
}
