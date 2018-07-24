package com.example.xianweili.newsapp.data.service;

import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xianweili on 7/24/18.
 */

public interface NewsListsService {
    @GET("/top-headlines")
    Observable<NewsListsResponse> getNewsList(@Query("country") String country,
                                               @Query("apiKey") String apiKey,
                                               @Query("page") int page,
                                               @Query("pagesize") int pagesize);
}
