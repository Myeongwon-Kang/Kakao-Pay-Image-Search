package com.genie97.myapplication.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.genie97.myapplication.contract.MainContract;
import com.genie97.myapplication.model.NetworkService;
import com.genie97.myapplication.model.SearchResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainViewModel {

    private static final String TAG = "MainViewModel";

    private static final String DEFAULT_SORT = "accuracy";
    private static boolean sortFlag = true;
    private static int current_page;
    private static final int DEFAULT_SIZE = 80;

    private static Timer timer = new Timer();

    private static MainContract mainContract;
    private static NetworkService networkService;

    public static ObservableField<String> search_query = new ObservableField<>("");

    public MainViewModel(MainContract mainContract, NetworkService networkService) {
        this.mainContract = mainContract;
        this.networkService = networkService;
    }

    private static long DELAY = 1000;

    private static boolean queryEmptyLock = false;

    public static void searchImages(int page) {
        Log.d(TAG, "searchImages()");
        cleanList();

        mainContract.initMaxSize();

        //current_page 1로 리셋
        current_page = page;

        // EditText empty인지 확인
        if (search_query.get().equals("")) {
            Log.d(TAG, "editText is empty");
            queryEmptyLock = true;
            queryEditTextEmpty();
        } else {
            queryEmptyLock = false;
            getQueryResult(search_query.get(), current_page);
        }
    }

    /*document response 값*/
    static List<String> imageUrlList = new ArrayList<>();
    static List<String> collectionList = new ArrayList<>();
    static List<String> thumbUrlList = new ArrayList<>();
    static List<String> displaySiteNameList = new ArrayList<>();
    static List<String> docUrlList = new ArrayList<>();
    static List<String> dateTimeList = new ArrayList<>();
    static List<Integer> imageWidthList = new ArrayList<>();
    static List<Integer> imageHeightList = new ArrayList<>();


    private static void queryEditTextEmpty() {
        cleanList();
        mainContract.displayInfo(imageUrlList, thumbUrlList, collectionList, displaySiteNameList, docUrlList, dateTimeList,imageWidthList,imageHeightList);
    }

    private static void getQueryResult(final String query, final int page) {
        Log.d(TAG, "getQueryResult()");
        timerStop();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String sort;
                if (sortFlag) {
                    sort = DEFAULT_SORT;
                } else {
                    sort = "recency";
                }
                Log.d(TAG, "sort type: " + sort);

                Call<SearchResult> getResult = networkService.getSearchResult(query, sort, page, DEFAULT_SIZE);
                getResult.enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        Log.d(TAG, "query: " + query + " page: " + page + " size: " + DEFAULT_SIZE);
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getResult - successful");
                            SearchResult.ResultMeta mMeta = response.body().getResultMeta();
                            int total_count = mMeta.getTotal_count();
                            int pageable_count = mMeta.getPageable_count();
                            boolean is_end = mMeta.getIs_end();

                            List<SearchResult.ResultDocuments> mList = response.body().getResultDocuments();
                            Log.d("TAG", mList.toString());
                            if (mList.isEmpty()) {
                                cleanList();
                                mainContract.setNoResult(true);
                                mainContract.createToast("\"" + query + "\"" + " 에 대한 검색 결과과 존재하지 않습니다.");
                            } else {
                                if (!queryEmptyLock) {
                                    for (SearchResult.ResultDocuments rd : mList) {
                                        Log.d(TAG, rd.getThumbnail_url().toString());
                                        Log.d(TAG, rd.getImage_url().toString());
                                        Log.d(TAG, rd.getDoc_url().toString());
                                        Log.d(TAG, rd.getDate_time().toString());
                                        Log.d(TAG, rd.getCollection().toString());

                                        imageUrlList.add(rd.getImage_url()); //이미지 가져오기
                                        imageWidthList.add(rd.getWidth()); //이미지 크기
                                        imageHeightList.add(rd.getHeight()); //이미지 크기
                                        thumbUrlList.add(rd.getThumbnail_url()); //썸네일
                                        docUrlList.add(rd.getDoc_url()); //문서 url
                                        collectionList.add(rd.getCollection()); //collection종류
                                        dateTimeList.add(rd.getDate_time()); //문서작성 시간
                                        displaySiteNameList.add(rd.getDisplay_sitename()); //출처
                                    }
                                    mainContract.setNoResult(false); // noResult layout visibility
                                    mainContract.setMaxSize(imageUrlList.size()); // scroll listener
                                    mainContract.displayInfo(imageUrlList, thumbUrlList, collectionList, displaySiteNameList, docUrlList, dateTimeList,imageWidthList,imageHeightList);
                                    mainContract.setResult(); // scroll listener
                                }
                            }
                        } else {
                            Log.d(TAG, response.code() + " **");
                            mainContract.createToast("Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        Log.e(TAG, t.getLocalizedMessage());
                        mainContract.createToast("Call getResult has failed: " + t.getLocalizedMessage());
                    }
                });

            }
        }, DELAY);
    }

    public static void loadMore() {
        Log.d(TAG, "loadeMore()");

        // next page
        current_page += 1;
        getQueryResult(search_query.get(), current_page);
    }

    private static void cleanList() {
        imageUrlList.clear();
        thumbUrlList.clear();
        collectionList.clear();
        displaySiteNameList.clear();
        docUrlList.clear();
        dateTimeList.clear();
        imageWidthList.clear();
        imageHeightList.clear();
    }
    public static void viewClick(String img_url, int width, int height, String doc_url){
        mainContract.moveActivity(img_url, width, height,doc_url);
    }

    public static void timerStop() {
        timer.cancel();
    }

}