package com.example.newsfeed.utils

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat

const val EMPTY_TEXT = ""
const val SUBLIST_LIMIT = 5
const val UNKNOWN = "Unknown"
const val DATE_FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DATE_FORMAT = "dd-MM-yyyy"
const val SOMETHING_WENT_WRONG = "Something went wrong"
const val NETWORK_ERROR = "No Internet Available.\n Please try again."
const val NO_NEWS_AVAILABLE = "No news available from source."

fun <T> List<T>.sublist(limit: Int): List<T> {
    return if (this.size < limit) {
        this
    } else {
        this.subList(0, limit)
    }
}

fun String.formatDate(): String {
    return try {
        val inputFormat = SimpleDateFormat(DATE_FORMAT_T)
        val outputFormat = SimpleDateFormat(DATE_FORMAT)
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        UNKNOWN
    }
}

inline fun TabLayout.addOnTabSelectedListener(crossinline callback: (Int) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            callback.invoke(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}

        override fun onTabReselected(tab: TabLayout.Tab) {}
    })
}

fun ImageView.loadUrl(url: String?) {
    url ?: return
    Glide.with(this.context).load(url).into(this)
}

fun SwipeRefreshLayout.toggleRefreshing() {
    this.isRefreshing = !this.isRefreshing
}