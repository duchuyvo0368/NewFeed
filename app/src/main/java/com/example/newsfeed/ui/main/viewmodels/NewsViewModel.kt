package com.example.newsfeed.ui.main.viewmodels

import androidx.lifecycle.*
import com.example.newsfeed.data.model.NewsModel
import com.example.newsfeed.data.repository.NewsRepository
import com.example.newsfeed.utils.Resource
import com.example.newsfeed.utils.SOMETHING_WENT_WRONG
import kotlinx.coroutines.Dispatchers

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    var newsList : List<NewsModel>? = null

    fun getAllNewsSources(apiKey: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = newsRepository.getAllNewsSources(apiKey = apiKey)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: SOMETHING_WENT_WRONG))
        }
    }

    fun getNewsFromSources(source: String, apiKey: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = newsRepository.getNewsFromSources(source = source, apiKey = apiKey)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: SOMETHING_WENT_WRONG))
        }
    }
}