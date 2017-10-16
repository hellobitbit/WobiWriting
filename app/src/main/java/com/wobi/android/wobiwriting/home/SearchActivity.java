package com.wobi.android.wobiwriting.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wobi.android.wobiwriting.R;
import com.wobi.android.wobiwriting.ui.BaseActivity;

/**
 * Created by wangyingren on 2017/10/16.
 */

public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        initViews();
    }

    private void initViews() {
        TextView cancelSearch = (TextView)findViewById(R.id.cancelSearch);
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
