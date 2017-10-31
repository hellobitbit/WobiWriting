package com.wobi.android.wobiwriting.moments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.moments.message.GetCityByCodeRequest;
import com.wobi.android.wobiwriting.moments.message.SearchOwnedCommunityRequest;
import com.wobi.android.wobiwriting.moments.model.CommunityInfo;
import com.wobi.android.wobiwriting.utils.LogUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/10/29.
 */

public class PersonalMomentsAdapter extends RecyclerView.Adapter<PersonalMomentsAdapter.SelectedViewHolder> {

    private static final String TAG = "PersonalMomentsAdapter";
    private final Context mContext;
    private final List<CommunityInfo> communityInfos;
    private final LayoutInflater mInflater;
    private final int user_id;
    private final Map<String, String> provinceMap;
    private OnRecyclerViewItemClickListener listener;
    private int selectedPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public PersonalMomentsAdapter(Context context, List<CommunityInfo> communityInfos,
                                  int user_id, Map<String, String> provinceMap){
        this.user_id = user_id;
        this.mContext = context;
        this.provinceMap = provinceMap;
        this.communityInfos = communityInfos;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setSelected(int position){
        selectedPosition = position;
    }


    @Override
    public SelectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedViewHolder(mInflater.inflate(R.layout.personal_moment_list_item_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return communityInfos.size();
    }

    public class SelectedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView moment_name;
        private final TextView moment_privacy;
        private final TextView moment_position;
        private final ImageView moment_owned;

        public SelectedViewHolder(View itemView) {
            super(itemView);
            moment_name = (TextView)itemView.findViewById(R.id.moment_name);
            moment_privacy = (TextView)itemView.findViewById(R.id.moment_privacy);
            moment_position = (TextView)itemView.findViewById(R.id.moment_position);
            moment_owned = (ImageView)itemView.findViewById(R.id.moment_owned);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            itemView.setTag(position);
            moment_name.setText(communityInfos.get(position).getCommunity_name());
            if (communityInfos.get(position).getIs_auth() == 0){
                moment_privacy.setText("公开");
            }else {
                moment_privacy.setText("私密");
            }

            if (user_id == communityInfos.get(position).getUser_id()){
                moment_owned.setVisibility(View.VISIBLE);
            }else {
                moment_owned.setVisibility(View.GONE);
            }
            moment_position.setText(provinceMap.get(communityInfos.get(position).getCity_code()));

//            searchOwnedCommunity(moment_position, communityInfos.get(position).getCity_code());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (Integer) itemView.getTag());
        }
    }

    private void searchOwnedCommunity(TextView moment_position, String city_code){
        GetCityByCodeRequest request = new GetCityByCodeRequest();
        request.setCity_code(city_code);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
            }
        });
    }
}
