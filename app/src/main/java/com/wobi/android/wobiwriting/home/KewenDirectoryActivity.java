package com.wobi.android.wobiwriting.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangyingren on 2017/10/5.
 */

public class KewenDirectoryActivity extends ActionBarActivity
        implements KwDirectoryAdapter.OnRecyclerViewItemClickListener{
    public static final String GRADE_ID = "grade_id";
    private static final String TAG = "KewenDirectoryActivity";
    private static final int REQUEST_CODE = 1006;
    private List<KeWenDirectory> mOriginDirectories =  new ArrayList<>();
    private List<KeWenDirectory> mDirectories =  new ArrayList<>();
    private List<JiaoCaiObject> mJCList = new ArrayList<>();
    private KwDirectoryAdapter mAdapter;
    private String grade_id;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kewen_directory_layout);
        grade_id = getIntent().getStringExtra(GRADE_ID);
        loadJCList();
        initViews();
        setCustomActionBar();
        updateTitleText("课文目录");
        updateImageResource(R.drawable.listen_writing_exit);
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
    }

    private void updateSelectedKewen(){
        int position = SharedPrefUtil.getKewenDirectoryPosition(getApplicationContext());
        mAdapter.setSelected(position);
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
                        mOriginDirectories = getKWMLListResponse.getKwmlList();
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
                    loadKWMLList(mJCList.get(0).getId());
                    mAdapter.setJc_id(mJCList.get(0).getId());
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
        boolean guestUser = SharedPrefUtil.getLoginInfo(getApplicationContext()).isEmpty();
        if (!guestUser ||(guestUser && position == 0)){
            updateKewenList(position);
        }else {
            checkLogin();
        }
    }

    @Override
    public void onSZItemClick() {
        setResult(0);
        finish();
    }

    private void updateKewenList(int position){
        SharedPrefUtil.saveKewenDirectoryPosition(getApplicationContext(),position);
        SharedPrefUtil.saveSZPosition(getApplicationContext(), 0);
        mAdapter.setSelected(position);
        if (position == mAdapter.getClicked()){
            mAdapter.resetClicked();
        }else {
            mAdapter.setClicked(position);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    protected int getActionBarRightButtonRes() {
        return 0;
    }

    @Override
    protected int getActionBarRightTitleRes() {
        return 0;
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
                startActivityForResult(intent,REQUEST_CODE);
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
        if (requestCode == REQUEST_CODE
                && resultCode == LoginActivity.RESULT_CODE_SUCCESS){
            mAdapter.notifyDataSetChanged();
        }
    }
}
