package com.wobi.android.wobiwriting.home.message;

import android.text.TextUtils;

import com.wobi.android.wobiwriting.data.message.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangyingren on 2017/9/23.
 */

public class GetSZInfoResponse extends Response implements Serializable {

    private String author1;
    private String author2;
    private String author3;
    private String author4;
    private String author5;

    private String banshu_url;
    private String bishun_url;
    private String maobi_url;
    private String swjz_url;
    private String swjz_zy_url;
    private String swjz_zxyb_url;
    private String swjz_xxsy_url;
    private String swjz_gs_url;
    private String wutizi_url;
    private String yingbi_url;
    private String zuci_url;

    private String bushou;
    private int duoyinzi;
    private String file;
    private String word;
    private String zxjg;

    private String pinyin;
    private String pinyin2;
    private String pinyin3;

    private String rename;

    private int sum1;
    private int sum2;
    private int sum3;
    private int sum4;
    private int sum5;

    private String zuci1;
    private String zuci2;
    private String zuci3;

    public String getAuthor1(){
        return author1;
    }

    public List<String> getAuthor1List(){
        List<String> authors = new ArrayList<>();
        if (author1 != null && !author1.isEmpty()){
            String[] authorArray = author1.split("/");
            authors.addAll(Arrays.asList(authorArray));
        }
        return authors;
    }

    public String getAuthor2(){
        return author2;
    }

    public List<String> getAuthor2List(){
        List<String> authors = new ArrayList<>();
        if (author2 != null && !author2.isEmpty()){
            String[] authorArray = author2.split("/");
            authors.addAll(Arrays.asList(authorArray));
        }
        return authors;
    }

    public String getAuthor3(){
        return author3;
    }

    public List<String> getAuthor3List(){
        List<String> authors = new ArrayList<>();
        if (author3 != null && !author3.isEmpty()){
            String[] authorArray = author3.split("/");
            authors.addAll(Arrays.asList(authorArray));
        }
        return authors;
    }

    public String getAuthor4(){
        return author4;
    }

    public List<String> getAuthor4List(){
        List<String> authors = new ArrayList<>();
        if (author4 != null && !author4.isEmpty()){
            String[] authorArray = author4.split("/");
            authors.addAll(Arrays.asList(authorArray));
        }
        return authors;
    }

    public String getAuthor5(){
        return  author5;
    }

    public List<String> getAuthor5List(){
        List<String> authors = new ArrayList<>();
        if (author5 != null && !author5.isEmpty()){
            String[] authorArray = author5.split("/");
            authors.addAll(Arrays.asList(authorArray));
        }
        return authors;
    }

    public String getBanshu_url(){
        return banshu_url;
    }

    public String getBishun_url(){
        return bishun_url;
    }

    public String getMaobi_url(){
        return maobi_url;
    }

    public String getSwjz_url(){
        return swjz_url;
    }

    public String getSwjz_zy_url(){
        return swjz_zy_url;
    }

    public String getSwjz_zxyb_url(){
        return swjz_zxyb_url;
    }

    public String getSwjz_gs_url(){
        return swjz_gs_url;
    }

    public String getSwjz_xxsy_url(){
        return swjz_xxsy_url;
    }

    public String getWutizi_url(){
        return wutizi_url;
    }

    public String getYingbi_url(){
        return yingbi_url;
    }

    public  String getZuci_url(){
        return zuci_url;
    }

    public String getBushou(){
        return bushou;
    }

    public String getFile(){
        return file;
    }

    public String getWord(){
        return word;
    }

    public String getZxjg(){
        return zxjg;
    }

    public int getDuoyinzi(){
        return duoyinzi;
    }

    public String getPinyin(){
        return pinyin;
    }

    public String getPinyin2(){
        return  pinyin2;
    }

    public String getPinyin3(){
        return pinyin3;
    }

    public String getRename(){
        return rename;
    }

    public int getSum1(){
        return sum1;
    }

    public int getSum2(){
        return  sum2;
    }

    public int getSum3(){
        return sum3;
    }

    public int getSum4(){
        return sum4;
    }

    public int getSum5(){
        return sum5;
    }

    public String getZuci1(){
        return zuci1;
    }

    public String getZuci2(){
        return zuci2;
    }

    public String getZuci3(){
        return zuci3;
    }

    public String getZuci(){
        if (TextUtils.isEmpty(zuci2) && TextUtils.isEmpty(zuci3)){
            return zuci1;
        }else if (TextUtils.isEmpty(zuci2)|| TextUtils.isEmpty(zuci3)){
            return zuci1+"、"+zuci2+zuci3;
        }else {
            return zuci1+"、"+zuci2+"、"+zuci3;
        }
    }
}
