package com.wobi.android.wobiwriting.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.SpaceItemDecoration;
import com.wobi.android.wobiwriting.home.message.GetSZListRequest;
import com.wobi.android.wobiwriting.home.message.GetSZListResponse;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/10/22.
 */

public class KwDirectoryAdapter extends RecyclerView.Adapter<KwDirectoryAdapter.DirectoryViewHolder> {

    private static final String TAG = "KwDirectoryAdapter";
    private Gson gson = new Gson();
    private final Context mContext;
    private Map<String, List<String>> szListMap = new HashMap<>();
    protected final LayoutInflater mInflater;
    protected List<KeWenDirectory> mDirectories;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition = -1;
    private int clickedPosition = -1;

    private int jc_id = 1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onSZItemClick(List<String> sz_list, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setJc_id(int jc_id){
        this.jc_id = jc_id;
    }

    public void setSelected(int position){
        this.selectedPosition = position;
    }

    public int getSelected(){
        return selectedPosition;
    }

    public void setClicked(int position){
        this.clickedPosition = position;
    }

    public int getClicked(){
        return clickedPosition;
    }

    public void resetClicked(){
        clickedPosition = -1;
    }

    public KwDirectoryAdapter(Context context, List<KeWenDirectory> directories) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDirectories = directories;
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryViewHolder(mInflater.inflate(R.layout.kewen_directory_list_item_layout,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDirectories.size();
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView directory_icon;
        private final ImageView directory_arrow;
        private final TextView title_view;
        private final RecyclerView directory_sz_list_recycler;
        private final RelativeLayout sz_layout;
        private DirectorySzAdapter mSZAdapter;
        private List<String> szList = new ArrayList<>();

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            title_view = (TextView) itemView.findViewById(R.id.directory_title);
            directory_icon = (ImageView) itemView.findViewById(R.id.directory_icon);
            directory_arrow = (ImageView) itemView.findViewById(R.id.directory_arrow);
            directory_sz_list_recycler = (RecyclerView)itemView.findViewById(R.id.directory_sz_list_recycler);
            setRecyclerView();
            sz_layout = (RelativeLayout) itemView.findViewById(R.id.sz_layout);
        }

        public void bind(int position) {
            String userInfo = SharedPrefUtil.getLoginInfo(mContext);
            UserGetInfoResponse userObj = gson.fromJson(userInfo, UserGetInfoResponse.class);
            if (TextUtils.isEmpty(userInfo)){
                updateSzInfoDisplayWhenGuest(position);
            }else {
                if (userObj.getIs_vip() == 1){
                    updateSzInfoDisplayWhenVip(position);
                }else {
                    updateSzInfoDisplayWhenLogin(position);
                }

            }
            updateSzRecycler(position);
            itemView.setTag(position);
            itemView.setOnClickListener(this);

            title_view.setText(mDirectories.get(position).getKewen());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }

        private void setRecyclerView(){
            //设置布局管理器
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext);
            linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            directory_sz_list_recycler.setLayoutManager(linearLayoutManager1);
            directory_sz_list_recycler.addItemDecoration(new SpaceItemDecoration(mContext, 9, 0));
            directory_sz_list_recycler.setHasFixedSize(true);
            //设置适配器
            mSZAdapter = new DirectorySzAdapter(mContext,szList);
            mSZAdapter.setOnItemClickListener(new DirectorySzAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SharedPrefUtil.saveSZPosition(mContext, position);
                    mSZAdapter.setSelected(position);
                    mSZAdapter.notifyDataSetChanged();
                    if (listener != null){
                        listener.onSZItemClick(szList, position);
                    }
                }
            });
            directory_sz_list_recycler.setAdapter(mSZAdapter);
        }

        private void updateSzInfoDisplayWhenLogin(int position){
            if (position == 0 || position ==1 || position == 2){
                if (clickedPosition == position){
                    title_view.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
                }else {
                    title_view.setTextColor(Color.parseColor("#fc5c59"));
                    directory_arrow.setImageResource(R.drawable.directory_arrow_down_red);
                }
                directory_icon.setImageResource(R.drawable.directory_icon_red_open);
            }else {
                directory_icon.setImageResource(R.drawable.kewen_directory_icon);
                title_view.setTextColor(Color.parseColor("#b0b0b0"));
                directory_arrow.setImageResource(R.drawable.directory_arrow_down);
            }
        }

        private void updateSzInfoDisplayWhenVip(int position){
            if (selectedPosition == position){
                if (clickedPosition == position){
                    directory_icon.setImageResource(R.drawable.directory_icon_red_open);
                    title_view.setTextColor(Color.parseColor("#fc5c59"));
                    directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
                }else {
                    title_view.setTextColor(Color.parseColor("#b0b0b0"));
                    directory_icon.setImageResource(R.drawable.directory_icon_red_closed);
                    directory_arrow.setImageResource(R.drawable.directory_arrow_down_red);
                }
            }else {
                title_view.setTextColor(Color.parseColor("#b0b0b0"));
                directory_icon.setImageResource(R.drawable.directory_icon_red_closed);
                directory_arrow.setImageResource(R.drawable.directory_arrow_down_red);
            }
        }

        private void updateSzInfoDisplayWhenGuest(int position){
            if (position == 0){
                if (clickedPosition == position){
                    title_view.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    directory_arrow.setImageResource(R.drawable.directory_arrow_right_red);
                }else {
                    title_view.setTextColor(Color.parseColor("#fc5c59"));
                    directory_arrow.setImageResource(R.drawable.directory_arrow_down_red);
                }
                directory_icon.setImageResource(R.drawable.directory_icon_red_open);
            }else {
                directory_icon.setImageResource(R.drawable.kewen_directory_icon);
                title_view.setTextColor(Color.parseColor("#b0b0b0"));
                directory_arrow.setImageResource(R.drawable.directory_arrow_down);
            }
        }

        private void updateSzRecycler(int position){
            if (position == clickedPosition){
                mSZAdapter.setSelected(SharedPrefUtil.getSZPosition(mContext));
                sz_layout.setVisibility(View.VISIBLE);
                if (szListMap.containsKey(mDirectories.get(position).getKwUrl())){
                    szList.clear();
                    szList.addAll(szListMap.get(mDirectories.get(position).getKwUrl()));
                    mSZAdapter.notifyDataSetChanged();
                    directory_sz_list_recycler.smoothScrollToPosition(SharedPrefUtil.getSZPosition(mContext));
                }else {
                    loadSZList(mDirectories.get(position).getKwUrl());
                }
            }else {
                sz_layout.setVisibility(View.GONE);
            }
        }

        private void loadSZList(final String kwUrl){
            GetSZListRequest request = new GetSZListRequest();
            request.setJcId(jc_id);
            request.setKwUrl(kwUrl);
            String jsonBody = request.jsonToString();
            NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
                @Override
                public void onSucceed(String response) {
                    LogUtil.d(TAG," response: "+response);
                    GetSZListResponse getSZListResponse = gson.fromJson(response, GetSZListResponse.class);
                    if (getSZListResponse != null && getSZListResponse.getHandleResult().equals("OK")){
                        if (getSZListResponse.getSzList() != null
                                && !getSZListResponse.getSzList().isEmpty()) {
                            szListMap.put(kwUrl,getSZListResponse.getSzList());
                            szList.clear();
                            szList.addAll(getSZListResponse.getSzList());
                            mSZAdapter.setSelected(SharedPrefUtil.getSZPosition(mContext));
                            mSZAdapter.notifyDataSetChanged();
                            directory_sz_list_recycler.smoothScrollToPosition(SharedPrefUtil.getSZPosition(mContext));
                        }else {
                            showErrorMsg("该课文暂没生字哦");
                        }
                    }else {
                        showErrorMsg("获取课文生字异常");
                    }

                }

                @Override
                public void onFailed(String errorMessage) {
                    LogUtil.e(TAG," error: "+errorMessage);
                    showNetWorkException();
                }
            });
        }
    }

    public void showErrorMsg(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void showNetWorkException(){
        showErrorMsg("网络异常");
    }
}
