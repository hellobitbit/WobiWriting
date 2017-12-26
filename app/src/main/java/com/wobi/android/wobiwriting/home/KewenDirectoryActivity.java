package com.wobi.android.wobiwriting.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.adapters.KwDirectoryAdapter;
import com.wobi.android.wobiwriting.home.message.GetJCListRequest;
import com.wobi.android.wobiwriting.home.message.GetJCListResponse;
import com.wobi.android.wobiwriting.home.message.GetKWMLListRequest;
import com.wobi.android.wobiwriting.home.message.GetKWMLListResponse;
import com.wobi.android.wobiwriting.home.model.JiaoCaiObject;
import com.wobi.android.wobiwriting.home.model.KeWenDirectory;
import com.wobi.android.wobiwriting.me.PurchaseVipActivity;
import com.wobi.android.wobiwriting.ui.BaseActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/5.
 */

public class KewenDirectoryActivity extends BaseActivity
        implements KwDirectoryAdapter.OnRecyclerViewItemClickListener{
    private static final String TAG = "KewenDirectoryActivity";
    private static final int LOGIN_REQUEST_CODE = 1006;
    private static final int SPEAK_REQUEST_CODE = 1007;
    private List<KeWenDirectory> mDirectories =  new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private KwDirectoryAdapter mAdapter;
    private String grade_id;
    private RecyclerView recyclerView;

    private int speakTypeValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kewen_directory_layout);
        grade_id = getIntent().getStringExtra(SpeakCNSzActivity.GRADE_ID);
        speakTypeValue = getIntent().getIntExtra(SpeakCNSzActivity.SPEAK_TYPE, 0);
        LogUtil.d(TAG, "onCreate speakTypeValue = "+speakTypeValue);
        loadJCList();
        initViews();
        updateSelectedKewen();
    }

    private void initViews() {

        recyclerView = (RecyclerView)findViewById(R.id.recycler_kewen_directory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 0, 9));

        mAdapter = new KwDirectoryAdapter(getApplicationContext(),mDirectories);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateSelectedKewen(){
        int position = SharedPrefUtil.getKewenDirectoryPosition(getApplicationContext());
        mAdapter.setSelected(position);
        mAdapter.setClicked(position);
        mAdapter.notifyDataSetChanged();
    }

    private void loadKWMLList(int jcId){
        GetKWMLListRequest request = new GetKWMLListRequest();
        request.setGradeId(grade_id);
        request.setJcId(jcId);
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetKWMLListResponse getKWMLListResponse = gson.fromJson(response, GetKWMLListResponse.class);
                if (getKWMLListResponse != null && getKWMLListResponse.getHandleResult().equals("OK")){
                    if (getKWMLListResponse.getKwmlList()!=null
                            && !getKWMLListResponse.getKwmlList().isEmpty()){
                        mDirectories.clear();
                        mDirectories.addAll(getKWMLListResponse.getKwmlList());
                        updateUI();
                    }else {
                        showErrorMsg("该教程目前没有课文哦");
                    }
                }else {
                    showErrorMsg("获取课文目录数据异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void loadJCList(){
        GetJCListRequest request = new GetJCListRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG," response: "+response);
                GetJCListResponse getJCListResponse = gson.fromJson(response, GetJCListResponse.class);
                if (getJCListResponse!=null && getJCListResponse.getHandleResult().equals("OK")){
                    mJCList.clear();
                    mJCList.addAll(getJCListResponse.getJcList());
                    int gradeid = Integer.parseInt(grade_id);
                    if (gradeid > 70){
                        loadKWMLList(10);
                        mAdapter.setJc_id(10);
                    }else {
                        loadKWMLList(1);
                        mAdapter.setJc_id(1);
                    }
                }else {
                    showErrorMsg("获取教程失败");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG," error: "+errorMessage);
                showNetWorkException();
            }
        });
    }

    private void updateUI(){
        Iterator<KeWenDirectory> it = mDirectories.iterator();
        while (it.hasNext()) {
            KeWenDirectory s = it.next();
            if (s.getKwUrl().equals("")) {
                it.remove();
            }
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mAdapter.getSelected());
    }

    @Override
    public void onItemClick(View view, int position) {
        String userInfo = SharedPrefUtil.getLoginInfo(getApplicationContext());
        UserGetInfoResponse userObj = gson.fromJson(userInfo, UserGetInfoResponse.class);
        boolean guestUser = TextUtils.isEmpty(userInfo);
        if (!guestUser && userObj.getIs_vip() == 1 || (!guestUser && userObj.getIs_vip() == 0 && position < 3)
                ||(guestUser && position == 0)){
            updateKewenList(position);
        }else if (!guestUser && userObj.getIs_vip() == 0 && position >= 3){
            checkVip();
        } else {
            checkLogin();
        }
    }

    @Override
    public void onSZItemClick(List<String> szList, int position) {
        int gradeId = Integer.parseInt(grade_id);
        if (gradeId > 70){
            Intent intent = new Intent(getApplicationContext(), SpeakCNScActivity.class);
            intent.putExtra(SpeakCNSzActivity.KEWEN_TITLE,mDirectories.get(mAdapter.getClicked()).getKewen());
            intent.putExtra(SpeakCNSzActivity.SPEAK_TYPE, speakTypeValue);
            intent.putStringArrayListExtra(SpeakCNScActivity.SZ_LIST, (ArrayList<String>) szList);

            startActivityForResult(intent, SPEAK_REQUEST_CODE);
        }else {
            Intent intent = new Intent(getApplicationContext(), SpeakCNSzActivity.class);
            intent.putExtra(SpeakCNSzActivity.KEWEN_TITLE,mDirectories.get(mAdapter.getClicked()).getKewen());
            intent.putExtra(SpeakCNSzActivity.SPEAK_TYPE, speakTypeValue);
            intent.putStringArrayListExtra(SpeakCNSzActivity.SZ_LIST, (ArrayList<String>) szList);

            startActivityForResult(intent, SPEAK_REQUEST_CODE);
        }

    }

    private void updateKewenList(int position){
        if (mAdapter.getSelected() != position){
            SharedPrefUtil.saveKewenDirectoryPosition(getApplicationContext(),position);
            SharedPrefUtil.saveSZPosition(getApplicationContext(), 0);
        }

        mAdapter.setSelected(position);
        if (position == mAdapter.getClicked()){
            mAdapter.resetClicked();
        }else {
            mAdapter.setClicked(position);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void checkVip(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("充值后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去充值", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getApplicationContext(), PurchaseVipActivity.class);
                startActivityForResult(intent, PurchaseVipActivity.REQUEST_CODE);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    protected void checkLogin(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("登录后才能使用此功能");
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,LOGIN_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"onActivityResult resultCode == "+resultCode+"  requestCode == "+requestCode);
        // 根据上面发送过去的请求吗来区别
        if (requestCode == LOGIN_REQUEST_CODE
                && resultCode == LoginActivity.RESULT_CODE_SUCCESS){
            mAdapter.notifyDataSetChanged();
        }else if (requestCode == SPEAK_REQUEST_CODE){
            mAdapter.notifyDataSetChanged();
        }else if (requestCode == PurchaseVipActivity.REQUEST_CODE){
            mAdapter.notifyDataSetChanged();
        }
    }
}
