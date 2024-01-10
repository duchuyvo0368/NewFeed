package com.example.newsfeed.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsSourceModel(

    @field:SerializedName("id")
    @Expose
    var id: String?,

    @field:SerializedName("name")
    @Expose
    var name: String?,

    @field:SerializedName("description")
    @Expose
    var description: String?,

    @field:SerializedName("url")
    @Expose
    var url: String?,

    @field:SerializedName("category")
    @Expose
    var category: String?,

    @field:SerializedName("language")
    @Expose
    var language: String?,

    @field:SerializedName("country")
    @Expose
    var country: String?
) : Parcelable

@Parcelize
data class NewsSources(

    @field:SerializedName("status")
    @Expose
    var status: String?,

    @field:SerializedName("sources")
    @Expose
    var sources: ArrayList<NewsSourceModel>?
) : Parcelable
