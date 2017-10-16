package com.wobi.android.wobiwriting.ui;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by wangyingren on 2017/10/14.
 */

public class BaseFragment extends Fragment {

    private ProgressDialog pd;

    Gson gson = new Gson();

    public void showErrorMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void showNetWorkException(){
        showErrorMsg("网络异常,请检查网络");
    }

    public void showDialog(String tips){
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.setMessage(tips);
        pd.show();
    }

    public void dismissDialog(){
        if (pd != null){
            pd.dismiss();
        }
    }
}
