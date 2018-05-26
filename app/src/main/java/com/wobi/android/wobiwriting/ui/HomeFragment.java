package com.wobi.android.wobiwriting.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.data.IResponseListener;
import com.wobi.android.wobiwriting.data.NetDataManager;
import com.wobi.android.wobiwriting.home.CNClassicActivity;
import com.wobi.android.wobiwriting.home.CalligraphyClassActivity;
import com.wobi.android.wobiwriting.home.KewenDirectoryActivity;
import com.wobi.android.wobiwriting.home.SearchActivity;
import com.wobi.android.wobiwriting.home.SpeakCNScActivity;
import com.wobi.android.wobiwriting.home.SpeakCNSzActivity;
import com.wobi.android.wobiwriting.home.adapters.AbstractSpinnerAdapter;
import com.wobi.android.wobiwriting.home.adapters.BannerViewpagerAdapter;
import com.wobi.android.wobiwriting.home.adapters.CustomSpinnerAdapter;
import com.wobi.android.wobiwriting.home.message.GetGradeRequest;
import com.wobi.android.wobiwriting.home.message.GetGradeResponse;
import com.wobi.android.wobiwriting.home.model.Grade;
import com.wobi.android.wobiwriting.upgrade.AppUpgradeManager;
import com.wobi.android.wobiwriting.user.LoginActivity;
import com.wobi.android.wobiwriting.user.RegisterActivity;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;
import com.wobi.android.wobiwriting.views.CustomDialog;
import com.wobi.android.wobiwriting.views.HomeItemView;
import com.wobi.android.wobiwriting.views.SpinnerPopWindow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangyingren on 2017/9/9.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,
        AbstractSpinnerAdapter.IOnItemSelectListener {
    private static final String TAG = "HomeFragment";
    private TextView textView;
    private SpinnerPopWindow mSpinnerPopWindow;
    private CustomSpinnerAdapter mAdapter;
    private int mSelected = -1;

    private List<Grade> mGradeList = new ArrayList<>();
    private ViewPager banner_viewpager;
    private HomeItemView cnClassic;
    private HomeItemView calligraghyClass;
    private HomeItemView chinese_writing;
    private HomeItemView speckCN;

    private boolean hidden = false;//fragment

    private boolean isContinue = true;
    private int index;
    //定时器，用于实现轮播
    private Timer timer;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    index++;
                    banner_viewpager.setCurrentItem(index);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag_layout, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkGradeInfo();

        mAdapter = new CustomSpinnerAdapter(getActivity());
        mAdapter.refreshData(mGradeList, 0);

        mSpinnerPopWindow = new SpinnerPopWindow(getActivity());
        mSpinnerPopWindow.setAdapter(mAdapter);
        mSpinnerPopWindow.setItemListener(this);

        mSpinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                textView.setBackgroundResource(R.drawable.home_grade_title_default_shape_corner);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scheduleTimer();

        if (!SharedPrefUtil.getHomeRegisterTipsState(getActivity())) {
            banner_viewpager.post(new Runnable() {
                @Override
                public void run() {
                    displayRegisterTipsWhenFirstLoaded();
                }
            });
        }

        banner_viewpager.post(new Runnable() {
            @Override
            public void run() {
                AppUpgradeManager.getInstance(getActivity()).checkUpdate(true);
            }
        });
    }

    private void initView(View view) {
        speckCN = (HomeItemView) view.findViewById(R.id.speak_chinese);
        cnClassic = (HomeItemView) view.findViewById(R.id.chinese_classic);
        chinese_writing = (HomeItemView) view.findViewById(R.id.chinese_writing);
        Intent cnClassicIntent = new Intent(getActivity(), CNClassicActivity.class);
        cnClassic.setMainIntent(cnClassicIntent, true);
        cnClassic.setSub1Intent(cnClassicIntent, true);
        cnClassic.setSub3Intent(cnClassicIntent, true);
        cnClassic.setSub4Intent(cnClassicIntent, true);

        calligraghyClass = (HomeItemView) view.findViewById(R.id.calligraghy_class);
        textView = (TextView) view.findViewById(R.id.dropdown);

        TextView search = (TextView) view.findViewById(R.id.search);
        search.setOnClickListener(this);

//        speckCN.setOnClickListener(this);
        textView.setOnClickListener(this);

        banner_viewpager = (ViewPager) view.findViewById(R.id.banner_viewpager);
        banner_viewpager.setAdapter(new BannerViewpagerAdapter(getActivity()));
        setViewPagerScrollSpeed();

        banner_viewpager.addOnPageChangeListener(onPageChangeListener);
        banner_viewpager.setOnTouchListener(onTouchListener);

        scheduleTimer();
    }

    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(banner_viewpager.getContext());
            mScroller.set(banner_viewpager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dropdown:
                showSpinWindow();
                break;
            case R.id. search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(SearchActivity.SEARCH_TYPE,"sz");
                startActivity(intent);
                break;
        }
    }

    private void showSpinWindow() {
        mSpinnerPopWindow.setWidth(textView.getWidth());
        mSpinnerPopWindow.showAsDropDown(textView);
        textView.setBackgroundResource(R.drawable.home_grade_title_selected_shape_corner);
    }

    @Override
    public void onItemClick(int pos) {
        refreshGradeInfo(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, " onDestroy ");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void refreshGradeInfo(int position) {
        mSelected = position;
        textView.setText(mGradeList.get(position).getGradeName());

        String gradeId = mGradeList.get(mSelected).getGradeId();
        if (!TextUtils.isEmpty(gradeId)){
            SharedPrefUtil.setGrade_ID(getActivity(), Integer.parseInt(gradeId.substring(0, 1)));
            SharedPrefUtil.setTerm_num(getActivity(), Integer.parseInt(gradeId.substring(1, 2)));
        }

        //swjz
        Intent swjzIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        swjzIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        swjzIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.SWJZ.getValue());
        speckCN.setMainIntent(swjzIntent, true);

        //swjz_zy
        Intent swjzZyIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        swjzZyIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        swjzZyIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.SWJZ_ZY.getValue());
        speckCN.setSub1Intent(swjzZyIntent, true);

        //swjz_zxyb
        Intent swjzZxybIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        swjzZxybIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        swjzZxybIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.SWJZ_ZXYB.getValue());
        speckCN.setSub2Intent(swjzZxybIntent, true);

        //swjz_xxsy
        Intent swjzXxsybIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        swjzXxsybIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        swjzXxsybIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.SWJZ_XXSY.getValue());
        speckCN.setSub3Intent(swjzXxsybIntent, true);

        //swjz_gxgs
        Intent swjzGxgsIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        swjzGxgsIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        swjzGxgsIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.SWJZ_GXGS.getValue());
        speckCN.setSub4Intent(swjzGxgsIntent, true);

        //shu fa ke tang
        Intent classIntent = new Intent(getActivity(), CalligraphyClassActivity.class);
        classIntent.putExtra(CalligraphyClassActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        calligraghyClass.setMainIntent(classIntent, true);
        calligraghyClass.setSub1Intent(classIntent, true);
        if (Integer.parseInt(gradeId) > 40) {
            calligraghyClass.setSub3Intent(classIntent, true);
        } else {
            calligraghyClass.setSub3Intent(classIntent, false, true);
        }

        //bishun
        Intent bishunIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        bishunIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        bishunIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.BISHUN.getValue());
        chinese_writing.setMainIntent(bishunIntent, true);
        chinese_writing.setSub1Intent(bishunIntent, true);
        if (Integer.parseInt(gradeId) > 70) {
            speckCN.setVisibility(View.GONE);
            chinese_writing.updateItem1Visibility(false);
        } else {
            speckCN.setVisibility(View.VISIBLE);
            chinese_writing.updateItem1Visibility(true);
        }

        //banshu
        Intent banshuIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        banshuIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        banshuIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.BANSHU.getValue());
        if (Integer.parseInt(gradeId) > 70) {
            banshuIntent.putExtra(SpeakCNScActivity.SPEAK_TYPE,
                    SpeakCNScActivity.SpeakType.BANSHU.getValue());
            chinese_writing.setMainIntent(banshuIntent, true);
        }
        chinese_writing.setSub2Intent(banshuIntent, true);


        //yingbi
        Intent yingbiIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        yingbiIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        yingbiIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.YINGBI.getValue());
        if (Integer.parseInt(gradeId) > 70) {
            yingbiIntent.putExtra(SpeakCNScActivity.SPEAK_TYPE,
                    SpeakCNScActivity.SpeakType.YINGBI.getValue());
        }
        chinese_writing.setSub3Intent(yingbiIntent, true);

        //maobi
        Intent maobiIntent = new Intent(getActivity(), KewenDirectoryActivity.class);
        maobiIntent.putExtra(SpeakCNSzActivity.GRADE_ID,
                mGradeList.get(position).getGradeId());
        maobiIntent.putExtra(SpeakCNSzActivity.SPEAK_TYPE,
                SpeakCNSzActivity.SpeakType.MAOBI.getValue());
        if (Integer.parseInt(gradeId) > 70) {
            maobiIntent.putExtra(SpeakCNScActivity.SPEAK_TYPE,
                    SpeakCNScActivity.SpeakType.MAOBI.getValue());
        }
        chinese_writing.setSub4Intent(maobiIntent, true);
    }

    private void checkGradeInfo() {
        if (mGradeList.size() == 0) {
            loadGradeInfo();
        }
    }

    private void loadGradeInfo() {
        GetGradeRequest request = new GetGradeRequest();
        String jsonBody = request.jsonToString();
        NetDataManager.getInstance().getMessageSender().sendEvent(jsonBody, new IResponseListener() {
            @Override
            public void onSucceed(String response) {
                LogUtil.d(TAG, " response: " + response);
                try {
                    GetGradeResponse getGradeResponse = gson.fromJson(response, GetGradeResponse.class);
                    if (getGradeResponse != null && getGradeResponse.getHandleResult().equals("OK")) {
                        if (getGradeResponse.getGradeList() != null) {
                            mGradeList.clear();
                            mGradeList.addAll(getGradeResponse.getGradeList());
                            refreshGradeInfo(0);
                        } else {
                            showErrorMsg("没有年级信息");
                        }
                    } else {
                        showErrorMsg("获取年级信息失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg("获取年级信息异常");
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                LogUtil.e(TAG, " error: " + errorMessage);
                showErrorMsg(errorMessage);
            }
        });
    }

    /**
     * 根据当前选中的页面设置按钮的选中
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            index = position;//当前位置赋值给索引
//            setCurrentDot(index%imageIds.length);
            LogUtil.d(TAG, "onPageSelected position = " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 根据当前触摸事件判断是否要轮播
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //手指按下和划动的时候停止图片的轮播
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                default:
                    isContinue = true;
            }
            return false;
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        LogUtil.d(TAG, " onHiddenChanged hidden = " + hidden);
        if (hidden) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            scheduleTimer();
            checkGradeInfo();
            AppUpgradeManager.getInstance(getActivity()).checkUpdate(true);
        }
    }

    private void scheduleTimer() {
        if (timer == null && !hidden) {
            timer = new Timer();//创建Timer对象
            //执行定时任务
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //首先判断是否需要轮播，是的话我们才发消息
                    if (isContinue) {
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }, 5000, 5000);//延迟5秒，每隔5秒发一次消息
        }
    }

    private void displayRegisterTipsWhenFirstLoaded() {
        LayoutInflater inflater = (LayoutInflater)getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_contentview_registertips_layout, null);
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessageType(CustomDialog.MessageType.TextView);
        builder.setTitle("提示");
        builder.setContentView(contentView);
        builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("home_start",true);
                startActivity(intent);
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
        SharedPrefUtil.saveHomeRegisterTipsState(getActivity(), true);
    }
}
