package com.example.newsfeed.ui.repository

import androidx.lifecycle.*
import com.example.newsfeed.data.api.ApiHelper
import com.example.newsfeed.data.repository.NewsRepository
import com.example.newsfeed.ui.main.viewmodels.NewsViewModel

class NewsModelRepository (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T  {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(NewsRepository(apiHelper = apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}