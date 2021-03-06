package com.wobi.android.wobiwriting.user.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.List;

/**
 * Created by wangyingren on 2018/5/12.
 */

public class JcListAdapter extends RecyclerView.Adapter<JcListAdapter.JcListViewHolder> {

    private static final String TAG = "JcListAdapter";
    private final View mParentView;
    private Gson gson = new Gson();
    private final Context mContext;
    protected final LayoutInflater mInflater;
    protected List<JiaoCaiObject> mJCList;
    private JcListAdapter.OnRecyclerViewItemClickListener listener;
    private int jc_id = -1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View parentView, View view, int position);
    }

    public void setOnItemClickListener(JcListAdapter.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setJcId(int jc_id){
        for (JiaoCaiObject object : mJCList){
            if (object.getId() == jc_id){
                this.jc_id = jc_id;
            }
        }
    }

    public int getJcId(){
        return jc_id;
    }

    public JcListAdapter(Context context, List<JiaoCaiObject> jcList, View parentView) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mJCList = jcList;
        mParentView = parentView;
    }

    @Override
    public JcListAdapter.JcListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JcListAdapter.JcListViewHolder(mInflater.inflate(R.layout.jc_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(JcListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mJCList.size();
    }

    public class JcListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title_view;
        private final ImageView jc_choose;

        public JcListViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.jc_name);
            jc_choose = (ImageView) itemView.findViewById(R.id.jc_choose);
        }

        public void bind(int position) {
            itemView.setTag(position);
            itemView.setOnClickListener(this);

            if (mJCList.get(position).getId() ==  jc_id){
                jc_choose.setImageResource(R.drawable.choose_selected);
            }else {
                jc_choose.setImageResource(R.drawable.choose_default);
            }

            title_view.setText(mJCList.get(position).getTeachName());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(mParentView, v, (Integer) itemView.getTag());
        }
    }

    public void showErrorMsg(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void showNetWorkException(){
        showErrorMsg("网络异常");
    }
}
