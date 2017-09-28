package com.wobi.android.wobiwriting.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.bean.ImageItem;
import com.wobi.android.wobiwriting.R;

import java.util.List;

/**
 * Created by wangyingren on 2017/9/19.
 */

public class TitlePickerAdapter extends RecyclerView.Adapter<TitlePickerAdapter.SelectedTitleViewHolder>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<String> mData;
    private OnRecyclerViewItemClickListener listener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public TitlePickerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public SelectedTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedTitleViewHolder(mInflater.inflate(R.layout.home_title_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedTitleViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedTitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title_view;
        private final View selectedLineView;
        private int clickPosition;

        public SelectedTitleViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.title);
            selectedLineView = (View) itemView.findViewById(R.id.selectedLine);
        }

        public void bind(int position) {
            //设置条目的点击事件
            title_view.setOnClickListener(this);
            title_view.setText(mData.get(position));
            //根据条目位置设置图片
//            ImageItem item = mData.get(position);
//            if (isAdded && position == getItemCount() - 1) {
//                iv_img.setImageResource(R.drawable.selector_image_add);
//                clickPosition = SendMomentActivity.IMAGE_ITEM_ADD;
//            } else {
//                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0);
//                clickPosition = position;
//            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}
