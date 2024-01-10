package com.example.newsfeed.ui.main.view.acitivties

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newsfeed.R
import com.example.newsfeed.data.api.ApiHelper
import com.example.newsfeed.data.api.RetrofitBuilder
import com.example.newsfeed.data.model.NewsModel
import com.example.newsfeed.data.model.NewsSourceModel
import com.example.newsfeed.data.model.NewsSources
import com.example.newsfeed.ui.main.adapter.OnItemClickListener
import com.example.newsfeed.ui.main.view.baseadapter.FragmentStateAdapter
import com.example.newsfeed.ui.main.view.fragments.NewsFragment
import com.example.newsfeed.ui.main.viewmodels.NewsViewModel
import com.example.newsfeed.ui.repository.NewsModelRepository
import com.example.newsfeed.utils.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_view.*

class NewsSourceActivity : AppCompatActivity(), OnItemClickListener {

    private var snackBar: Snackbar? = null
    private lateinit var viewModel: NewsViewModel
    private var newsSourceList: List<NewsSourceModel>? = null

    private val viewPageStateAdapter: FragmentStateAdapter by lazy {
        return@lazy FragmentStateAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setupViewModel()
        this.setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, NewsModelRepository(ApiHelper(RetrofitBuilder.apiService))).get(NewsViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.getAllNewsSources(apiKey = getString(R.string.api_key))
            .observe(this, Observer {
                it?.let { resource ->
                    snackBar?.dismiss()
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar?.visibility = View.GONE
                            tv_error?.visibility = View.GONE
                            tabs?.visibility = View.VISIBLE
                            resource.data?.let { sources -> setupUI(sources = sources) }
                        }
                        Status.ERROR -> {
                            progressBar?.visibility = View.GONE
                            tv_error?.visibility = View.VISIBLE
                            tv_error?.text = NETWORK_ERROR
                            tabs?.visibility = View.GONE

                            displaySnackbar()
                        }
                        Status.LOADING -> {
                            progressBar?.visibility = View.VISIBLE
                            tv_error?.visibility = View.GONE
                            tabs?.visibility = View.GONE
                        }
                    }
                }
            })
    }

    private fun displaySnackbar(): Unit? {
        snackBar = Snackbar.make(rootView, SOMETHING_WENT_WRONG, Snackbar.LENGTH_LONG)
            .setAction("Try Again", View.OnClickListener {
                setupObservers()
            })
        snackBar?.setActionTextColor(Color.WHITE)
        snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        return snackBar?.show()
    }

    private fun setupUI(sources: NewsSources) {
        newsSourceList = sources.sources?.sublist(limit = SUBLIST_LIMIT)
        val newsSourceList = newsSourceList ?: return

        newsSourceList.forEach { model ->
            viewPageStateAdapter.addFragment(fragment = NewsFragment.newInstance(onItemClickListener = this), title = model.name ?: UNKNOWN)
        }

        // Add adapter
        pager?.offscreenPageLimit = newsSourceList.size
        pager?.adapter = viewPageStateAdapter
        tabs?.setupWithViewPager(pager, true)

        this.activateNewsFragment(position = 0)

        // Add callback listener
        tabs.addOnTabSelectedListener { position ->
            activateNewsFragment(position = position)
        }
    }

    private fun activateNewsFragment(position: Int) {
        // Update adapter position
        pager.currentItem = position

        (viewPageStateAdapter.getItem(position) as? NewsFragment)?.apply {
            newsSourceList?.get(position)?.let {
                this.setupObservers(sourceModel = it)
            }
        }
    }

    override fun onItemClicked(sourceModel: NewsModel) {
        WebActivity.newInstance(
            context = this,
            url = sourceModel.url ?: UNKNOWN,
            title = sourceModel.title ?: UNKNOWN
        )
    }
}
