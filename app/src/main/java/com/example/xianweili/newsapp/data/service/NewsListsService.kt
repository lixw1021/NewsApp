package com.example.xianweili.newsapp.data.service

import retrofit2.http.GET
import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse
import io.reactivex.Observable
import retrofit2.http.Query

interface NewsListsService {
    @GET("v2/top-headlines")
    fun getNewsList(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?,
        @Query("page") page: Int,
        @Query("pageSize") pagesize: Int
    ): Observable<NewsListsResponse?>?
}