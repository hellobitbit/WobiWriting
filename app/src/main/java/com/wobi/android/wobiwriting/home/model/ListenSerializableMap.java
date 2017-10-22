package com.wobi.android.wobiwriting.home.model;

import com.wobi.android.wobiwriting.home.message.GetSZInfoResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangyingren on 2017/10/21.
 */

public class ListenSerializableMap implements Serializable{

    private String title;
    private ArrayList<String> szList = new ArrayList<>();
    private HashMap<String, GetSZInfoResponse> szInfoResponseMap = new HashMap<>();

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setSzList(ArrayList<String> szList){
        this.szList.clear();
        this.szList.addAll(szList);
    }

    public ArrayList<String> getSzList(){
        return szList;
    }

    public void setSzInfoResponseMap(HashMap<String, GetSZInfoResponse> szInfoResponseMap){
        this.szInfoResponseMap.clear();
        this.szInfoResponseMap.putAll(szInfoResponseMap);
    }

    public HashMap<String, GetSZInfoResponse> getSzInfoResponseMap(){
        return szInfoResponseMap;
    }
}
