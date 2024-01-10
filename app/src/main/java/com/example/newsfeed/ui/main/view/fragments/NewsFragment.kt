package com.example.newsfeed.ui.main.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfeed.R
import com.example.newsfeed.data.api.ApiHelper
import com.example.newsfeed.data.api.RetrofitBuilder
import com.example.newsfeed.data.model.NewsModel
import com.example.newsfeed.data.model.NewsSourceModel
import com.example.newsfeed.ui.main.adapter.NewsSourceAdapter
import com.example.newsfeed.ui.main.adapter.OnItemClickListener
import com.example.newsfeed.ui.main.viewmodels.NewsViewModel
import com.example.newsfeed.ui.repository.NewsModelRepository
import com.example.newsfeed.utils.*
import kotlinx.android.synthetic.main.common_progress_view.*
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment(), OnItemClickListener {

    companion object {
        fun newInstance(onItemClickListener: OnItemClickListener) = NewsFragment().apply {
            this.onItemClickListener = onItemClickListener
        }
    }

    // Variables
    private var sourceModel: NewsSourceModel? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsSourceAdapter: NewsSourceAdapter
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupViewModel()
        this.setupUI()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, NewsModelRepository(ApiHelper(RetrofitBuilder.apiService))).get(NewsViewModel::class.java)
    }

    private fun setupUI() {
        context?.apply {
            newsSourceAdapter = NewsSourceAdapter(newsSourceModels = arrayListOf(), onItemClickListener = onItemClickListener)
            recyclerView?.layoutManager = LinearLayoutManager(this)
            recyclerView?.adapter = newsSourceAdapter

            sv_refresh_layout?.setOnRefreshListener {
                setupObservers(sourceModel = sourceModel)
            }
        }
    }

    fun setupObservers(sourceModel: NewsSourceModel?) {
        if (viewModel.newsList?.isNullOrEmpty() == false) {
            sv_refresh_layout?.isRefreshing = false
            this.retrieveList(sources = viewModel.newsList)
            return
        }

        // Store in-memory source model
        this.sourceModel = sourceModel
        viewModel.getNewsFromSources(source = sourceModel?.id ?: EMPTY_TEXT, apiKey = getString(R.string.api_key))
            .observe(this, Observer {
                it?.let { resource ->
                    sv_refresh_layout?.toggleRefreshing()
                    when (resource.status) {
                        Status.SUCCESS -> {
                            recyclerView.visibility = View.VISIBLE
                            tv_error.visibility = View.GONE
                            resource.data?.let { sources -> retrieveList(sources = sources.newsList) }
                        }
                        Status.ERROR -> {
                            noNewAvailable(message = it.message)
                        }
                        Status.LOADING -> {
                            recyclerView.visibility = View.GONE
                            tv_error.visibility = View.GONE
                        }
                    }
                }
            })
    }

    private fun noNewAvailable(message: String?) {
        recyclerView?.visibility = View.GONE
        tv_error?.visibility = View.VISIBLE

        // Mark text values
        tv_error?.text = NETWORK_ERROR
        sv_refresh_layout?.isRefreshing = false
    }

    private fun retrieveList(sources: List<NewsModel>?) {
        viewModel.newsList = sources ?: return
        newsSourceAdapter.addNNewsSourceResults(sources = sources)
    }

    override fun onItemClicked(sourceModel: NewsModel) {
        onItemClickListener.onItemClicked(sourceModel = sourceModel)
    }
}
