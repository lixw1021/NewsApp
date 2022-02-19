package com.example.xianweili.newsapp.data

import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse
import com.example.xianweili.newsapp.data.service.NewsListsService
import io.reactivex.Observable
import retrofit2.http.Query
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val service: NewsListsService
) {
    fun getNewsList(
        country: String,
        page: Int,
        pageSize: Int
    ): Observable<NewsListsResponse> = service.getNewsList(
        country = country,
        apiKey = "84cce694de214d928bf162917492d2c0",
        page = page,
        pageSize = pageSize
    )
}