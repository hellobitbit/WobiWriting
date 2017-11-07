package com.wobi.android.wobiwriting.moments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bigkoo.pickerview.OptionsPickerView;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.db.AreaBean;
import com.wobi.android.wobiwriting.db.CityBean;
import com.wobi.android.wobiwriting.db.DBManager;
import com.wobi.android.wobiwriting.db.ProvinceBean;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesRequest;
import com.wobi.android.wobiwriting.moments.message.GetAllProvincesResponse;
import com.wobi.android.wobiwriting.moments.model.Province;
import com.wobi.android.wobiwriting.ui.ActionBarActivity;
import com.wobi.android.wobiwriting.user.message.UserGetInfoResponse;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;
import com.wobi.android.wobiwriting.views.CustomSettingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyingren on 2017/10/30.
 */

public class NewOrModifyMomentBaseActivity extends ActionBarActivity implements View.OnClickListener{
    private static final String TAG = "NewOrModifyMomentBaseActivity";
    UserGetInfoResponse userInfo;

    String community_name = "";
    String community_description = "";
    String community_address = "";
    boolean isAuth = true;

    String city_code = "";

    private CustomSettingBar moment_name_bar;
    private CustomSettingBar moment_description_bar;
    private CustomSettingBar moment_position_bar;
    private CustomSettingBar moment_permission_bar;

    private OptionsPickerView pvOptions;//地址选择器
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();//省
    private ArrayList<ArrayList<CityBean>> options2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items = new ArrayList<>();//区
    private ArrayList<String> Provincestr = new ArrayList<>();//省
    private ArrayList<ArrayList<String>> Citystr = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> Areastr = new ArrayList<>();//区

    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moment_layout);
        String useInfoStr = SharedPrefUtil.getLoginInfo(getApplicationContext());
        userInfo = gson.fromJson(useInfoStr,UserGetInfoResponse.class);
        initViews();
        setCustomActionBar();
        refreshUI();
        loadDbData();
    }

    private void loadDbData(){
        showDialog("正在加载数据，请耐心等待");
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadCityData();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                        updateCityViewDisplay();
                    }
                });
            }
        }).start();
    }

    private void initViews() {
        //选项选择器
        pvOptions = new OptionsPickerView(this);

        moment_name_bar = (CustomSettingBar)findViewById(R.id.moment_name_bar);
        moment_description_bar = (CustomSettingBar)findViewById(R.id.moment_description_bar);
        moment_position_bar = (CustomSettingBar)findViewById(R.id.moment_position_bar);
        moment_permission_bar = (CustomSettingBar)findViewById(R.id.moment_permission_bar);

        moment_name_bar.setOnClickListener(this);
        moment_description_bar.setOnClickListener(this);
        moment_position_bar.setOnClickListener(this);
        moment_permission_bar.setOnClickListener(this);
    }

    void refreshUI(){
        if (TextUtils.isEmpty(community_name)){
            moment_name_bar.setRightText("无");
        }else {
            moment_name_bar.setRightText(community_name);
        }

        if (TextUtils.isEmpty(community_description)){
            moment_description_bar.setRightText("无");
        }else {
            moment_description_bar.setRightText(community_description);
        }

        if (TextUtils.isEmpty(community_address)){
            moment_position_bar.setRightText("无");
        }else {
            moment_position_bar.setRightText(community_address);
        }

        if (isAuth){
            moment_permission_bar.setRightText("需要验证");
        }else {
            moment_permission_bar.setRightText("不需要验证");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.moment_name_bar:
                displayNameDialog();
                break;
            case R.id.moment_description_bar:
                displayDescriptionDialog();
                break;
            case R.id.moment_position_bar:
                displayAddressDialog();
                break;
            case R.id.moment_permission_bar:
                displayPermissionDialog();
                break;
        }
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

    private void displayNameDialog(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(community_name);
        builder.setHint("请输入沃笔圈名");
        builder.setMessageType(CustomDialog.MessageType.EditText);
        builder.setTitle("圈名修改");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                CustomDialog customDialog =(CustomDialog)dialog;
                EditText editText = (EditText) customDialog.findViewById(R.id.edit_message);
                LogUtil.d(TAG, "editText = "+editText.getText().toString());
                community_name = editText.getText().toString();
                refreshUI();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void displayDescriptionDialog(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(community_description);
        builder.setHint("让我们一起创建良好的学习气氛.");
        builder.setMessageType(CustomDialog.MessageType.EditText);
        builder.setTitle("介绍修改");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                CustomDialog customDialog =(CustomDialog)dialog;
                EditText editText = (EditText) customDialog.findViewById(R.id.edit_message);
                LogUtil.d(TAG, "editText = "+editText.getText().toString());
                community_description = editText.getText().toString();
                refreshUI();

            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void displayAddressDialog(){
        if (pvOptions != null) {
            pvOptions.show();
        }
    }

    private void displayPermissionDialog(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_contentview_radiobutton_layout, null);
        final RadioButton needVerify = (RadioButton)contentView.findViewById(R.id.needVerify);
        RadioButton noVerify = (RadioButton)contentView.findViewById(R.id.noVerify);
        if (isAuth == true){
            needVerify.setChecked(true);
        }else {
            noVerify.setChecked(true);
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("进入圈子的时候");
        builder.setContentView(contentView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                if (needVerify.isChecked()){
                    isAuth =  true;
                }else {
                    isAuth = false;
                }
                refreshUI();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void loadCityData(){
        // 获取数据库
        SQLiteDatabase db = DBManager.getdb(getApplication());
        //省
        Cursor cursor = db.query("province", null, null, null, null, null,
                null);
        while (cursor.moveToNext()) {
            int pro_id = cursor.getInt(0);
            String pro_code = cursor.getString(1);
            String pro_name = cursor.getString(2);
            String pro_name2 = cursor.getString(3);
            ProvinceBean provinceBean = new ProvinceBean(pro_id, pro_code, pro_name, pro_name2);
            options1Items.add(provinceBean);//添加一级目录
            Provincestr.add(cursor.getString(2));
            //查询二级目录，市区
            Cursor cursor1 = db.query("city", null, "province_id=?", new String[]{pro_id + ""}, null, null,
                    null);
            ArrayList<CityBean> cityBeanList = new ArrayList<>();
            ArrayList<String> cityStr = new ArrayList<>();
            //地区集合的集合（注意这里要的是当前省份下面，当前所有城市的地区集合我去）
            ArrayList<ArrayList<AreaBean>> options3Items_03 = new ArrayList<>();
            ArrayList<ArrayList<String>> options3Items_str = new ArrayList<>();
            while (cursor1.moveToNext()) {
                int cityid = cursor1.getInt(0);
                int province_id = cursor1.getInt(1);
                String code = cursor1.getString(2);
                String name = cursor1.getString(3);
                String provincecode = cursor1.getString(4);
                CityBean cityBean = new CityBean(cityid, province_id, code, name, provincecode);
                //添加二级目录
                cityBeanList.add(cityBean);
                cityStr.add(cursor1.getString(3));
                //查询三级目录
                Cursor cursor2 = db.query("area", null, "city_id=?", new String[]{cityid + ""}, null, null,
                        null);
                ArrayList<AreaBean> areaBeanList = new ArrayList<>();
                ArrayList<String> areaBeanstr = new ArrayList<>();
                while (cursor2.moveToNext()) {
                    int areaid = cursor2.getInt(0);
                    int city_id = cursor2.getInt(1);
//                    String code0=cursor2.getString(2);
                    String areaname = cursor2.getString(3);
                    String citycode = cursor2.getString(4);
                    AreaBean areaBean = new AreaBean(areaid, city_id, areaname, citycode);
                    areaBeanList.add(areaBean);
                    areaBeanstr.add(cursor2.getString(3));
                }
                if (cursor2 != null){
                    cursor2.close();
                }
                options3Items_str.add(areaBeanstr);//本次查询的存储内容
                options3Items_03.add(areaBeanList);
            }
            if (cursor1 != null){
                cursor1.close();
            }
            options2Items.add(cityBeanList);//增加二级目录数据
            Citystr.add(cityStr);
            options3Items.add(options3Items_03);//添加三级目录
            Areastr.add(options3Items_str);
        }
        if (cursor != null){
            cursor.close();
        }
    }

    private void updateCityViewDisplay() {

        //设置三级联动效果
        pvOptions.setPicker(Provincestr, Citystr, Areastr, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        //设置是否循环滚动
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPro_name()
                        + options2Items.get(options1).get(option2).getName()
                        + options3Items.get(options1).get(option2).get(options3).getName();
                community_address = tx;
                city_code = options1Items.get(options1).getPro_code();
                LogUtil.d(TAG,"city_code = "+city_code);
                refreshUI();
            }
        });
    }
}
