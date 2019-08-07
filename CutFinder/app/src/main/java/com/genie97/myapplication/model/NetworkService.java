package com.genie97.myapplication.model;

import retrofit2.Call;
import retrofit2.http.*;

public interface NetworkService {

    @Headers("Authorization: KakaoAK ea1ae95890053f2d250ae6d74156d4d6")
    @GET("/v2/search/image")
    Call<SearchResult> getSearchResult(
            // 검색어
            @Query("query") String query,
            // 검색어 소팅 (default: accuracy, recency)
            @Query("sort") String sort,
            // 결과 페이지 (default: 1)
            @Query("page") int page,
            // 한 페이지 결과 개수 (default: 80)
            @Query("size") int size
    );

}