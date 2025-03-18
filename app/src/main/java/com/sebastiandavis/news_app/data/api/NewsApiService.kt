package com.sebastiandavis.news_app.data.api

import com.sebastiandavis.news_app.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String
    ): NewsResponse
} 