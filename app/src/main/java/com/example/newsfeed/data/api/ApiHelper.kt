package com.example.newsfeed.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getAllNewsSources(apiKey: String) = apiService.getAllNewsSources(apiKey = apiKey)

    suspend fun getNewsFromSources(source: String, apiKey: String) = apiService.getNewsFromSources(source = source, apiKey = apiKey)
}