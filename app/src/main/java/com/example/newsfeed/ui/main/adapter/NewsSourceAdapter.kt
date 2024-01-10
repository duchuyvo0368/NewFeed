package com.example.newsfeed.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.R
import com.example.newsfeed.data.model.NewsModel
import com.example.newsfeed.utils.UNKNOWN
import com.example.newsfeed.utils.formatDate
import com.example.newsfeed.utils.loadUrl
import kotlinx.android.synthetic.main.item_layout.view.*

interface OnItemClickListener {
    fun onItemClicked(sourceModel: NewsModel)
}

class NewsSourceAdapter(private val newsSourceModels: ArrayList<NewsModel>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<NewsSourceAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sourceModel: NewsModel) {
            itemView.apply {
                tv_title?.text = sourceModel.title
                tv_description?.text = "Author: ${sourceModel.author?.capitalize() ?: UNKNOWN} "
                tv_category?.text = "Published At: ${sourceModel.publishedAt?.formatDate() ?: UNKNOWN}"
                iv_icon?.loadUrl(url = sourceModel.urlToImage)

                cardView.setOnClickListener {
                    onItemClickListener.onItemClicked(sourceModel = sourceModel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder = DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))

    override fun getItemCount(): Int = newsSourceModels.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(sourceModel = newsSourceModels[position])
    }

    fun addNNewsSourceResults(sources: List<NewsModel>) {
        this.newsSourceModels.apply {
            clear()
            addAll(sources)
            notifyDataSetChanged()
        }
    }
}