package com.wobi.android.wobiwriting.ui;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class BaseFragment extends Fragment {

    Gson gson = new Gson();

    public void showErrorMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void showNetWorkException(){
        showErrorMsg("网络异常");
    }
}
