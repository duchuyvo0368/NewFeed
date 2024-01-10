package com.example.newsfeed.data.api

import com.example.newsfeed.data.model.News
import com.example.newsfeed.data.model.NewsSources
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("sources")
    suspend fun getAllNewsSources(@Query("language") language: String = "en", @Query("apiKey") apiKey: String): NewsSources

    @GET("everything")
    suspend fun getNewsFromSources(@Query("language") language: String = "en", @Query("q") source: String, @Query("apiKey") apiKey: String): News
}