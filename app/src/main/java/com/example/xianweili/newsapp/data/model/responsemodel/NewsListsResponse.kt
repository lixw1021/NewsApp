package com.example.xianweili.newsapp.data.model.responsemodel

data class NewsListsResponse(
    var status: String? = null,
    var totalResults: Int? = null,
    var articles: List<Article>,
)