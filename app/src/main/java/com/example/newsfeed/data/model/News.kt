package com.example.newsfeed.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Parcelize
data class Source(
    @field:SerializedName("id")
    @Expose
    var id: String?,

    @field:SerializedName("name")
    @Expose
    var name: String?
) : Parcelable

@Parcelize
data class NewsModel(

    @field:SerializedName("author")
    @Expose
    var author: String?,

    @field:SerializedName("title")
    @Expose
    var title: String?,

    @field:SerializedName("description")
    @Expose
    var description: String?,

    @field:SerializedName("url")
    @Expose
    var url: String?,

    @field:SerializedName("urlToImage")
    @Expose
    var urlToImage: String?,

    @field:SerializedName("publishedAt")
    @Expose
    var publishedAt: String?,

    @field:SerializedName("content")
    @Expose
    var content: String?
) : Parcelable

@Parcelize
data class News(
    @field:SerializedName("status")
    @Expose
    var status: String?,

    @field:SerializedName("totalResults")
    @Expose
    var totalResults: Int?,

    @field:SerializedName("articles")
    @Expose
    var newsList: List<NewsModel>?
) : Parcelable
