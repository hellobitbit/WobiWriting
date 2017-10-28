package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/28.
 */

public class WutiziInfoAdapter extends RecyclerView.Adapter<WutiziInfoAdapter.SelectedWutiziViewHolder> {

    private static final String TAG = "WutiziInfoAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final DisplayImageOptions options;
    private List<String> mData = new ArrayList<>();
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition = 0;
    private GetSZInfoResponse szInfo;
    private ImageLoader imageLoader;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public WutiziInfoAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(15))
                .build();
    }

    public void setSelected(int position){
        selectedPosition = position;
        if (szInfo != null) {
            if (selectedPosition == 0) {
                mData.clear();
                mData.addAll(szInfo.getAuthor1List());
            }else if (selectedPosition == 1){
                mData.clear();
                mData.addAll(szInfo.getAuthor2List());
            }else if (selectedPosition == 2){
                mData.clear();
                mData.addAll(szInfo.getAuthor3List());
            }else if (selectedPosition == 3){
                mData.clear();
                mData.addAll(szInfo.getAuthor4List());
            }else if (selectedPosition == 4){
                mData.clear();
                mData.addAll(szInfo.getAuthor5List());
            }
        }
    }

    public void setSzInfo(GetSZInfoResponse szInfo){
        this.szInfo = szInfo;
    }


    @Override
    public SelectedWutiziViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedWutiziViewHolder(mInflater.inflate(R.layout.wuti_info_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedWutiziViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedWutiziViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView wutizi_icon;
        private TextView author;

        public SelectedWutiziViewHolder(View itemView) {
            super(itemView);
            wutizi_icon = (ImageView) itemView.findViewById(R.id.wutizi_icon);
            author = (TextView) itemView.findViewById(R.id.author);

        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setTag(position);
            author.setText(mData.get(position));

            if (szInfo != null) {
                String url = szInfo.getWutizi_url()+szInfo.getFile()+"/"+(selectedPosition+1)
                        +"/"+"b"+(position+1)+".png";
                LogUtil.d(TAG,"bind url = "+url);
                imageLoader.displayImage(url, wutizi_icon, options);
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }
}
