package com.wobi.android.wobiwriting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.adpaters.GuideViewpagerAdapter;
import com.wobi.android.wobiwriting.utils.LogUtil;
import com.wobi.android.wobiwriting.utils.SharedPrefUtil;

/**
 * Created by wangyingren on 2017/10/15.
 */

public class GuideActivity extends BaseActivity {

    private static final String TAG = "GuideActivity";
    private Handler handler = new Handler();
    private ViewPager guide_viewpager;
    private int imgIds[] = {R.drawable.guide_icon1, R.drawable.guide_icon2, R.drawable.guide_icon3};

    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;
    private ImageView splash_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guide_layout);
        StatusBarUtil.setTransparent(this);
        initViews();

    }

    private void initViews() {
        if (!SharedPrefUtil.getGuideState(getApplicationContext())) {
            guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
            guide_viewpager.setVisibility(View.VISIBLE);
            guide_viewpager.setAdapter(new GuideViewpagerAdapter(getApplicationContext(), imgIds));

            guide_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                /**
                 * 在屏幕滚动过程中不断被调用
                 *
                 * @param position
                 * @param positionOffset       是当前页面滑动比例，如果页面向右翻动，这个值不断变大，最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0
                 * @param positionOffsetPixels 是当前页面滑动像素，变化情况和positionOffset一致
                 */
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    LogUtil.v(TAG, isLastPage + "   " + isDragPage + "   " + positionOffsetPixels);
                    if (isLastPage && isDragPage && positionOffsetPixels == 0) {
                        //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                        if (canJumpPage) {
                            canJumpPage = false;
                            JumpToMainActivity();
                        }
                    }
                }

                /**
                 * 这个方法有一个参数position，代表哪个页面被选中
                 *
                 * @param position 当前页的索引
                 */
                @Override
                public void onPageSelected(int position) {
                    isLastPage = position == imgIds.length - 1;
                }

                /**
                 * 在手指操作屏幕的时候发生变化
                 *
                 * @param state 有三个值：0（END）,1(PRESS) , 2(UP) 。
                 */
                @Override
                public void onPageScrollStateChanged(int state) {
                    isDragPage = state == 1;
                }
            });
        }else {
            splash_icon = (ImageView) findViewById(R.id.splash_icon);
            splash_icon.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 3000);
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            JumpToMainActivity();
        }
    };

    private void JumpToMainActivity(){
        SharedPrefUtil.saveGuideState(getApplicationContext(), true);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
