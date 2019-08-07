package com.genie97.myapplication.contract;

import java.util.List;

public interface MainContract {

    void displayInfo(List<String>mList, List<String> thumbNailList,
                     List<String> collectionList, List<String> siteNameList,
                     List<String> docUrlList,List<String> dateTimeList,List<Integer>width,List<Integer>height);
    void initMaxSize();
    void setMaxSize(int max);
    void setResult();
    void setNoResult(boolean flag);
    void createToast(String text);
    void moveActivity(String img_url, int width, int height, String doc_url);
}