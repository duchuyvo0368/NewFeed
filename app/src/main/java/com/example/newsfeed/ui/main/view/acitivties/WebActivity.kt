package com.example.newsfeed.ui.main.view.acitivties

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsfeed.R
import com.example.newsfeed.utils.UNKNOWN
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    companion object {
        private const val BUNDLE_URL = "bundle_key_url"
        private const val BUNDLE_TITLE = "bundle_key_title"

        fun newInstance(context: Context, url: String, title: String) {
            Intent(context, WebActivity::class.java).apply {
                putExtra(BUNDLE_URL, url)
                putExtra(BUNDLE_TITLE, title)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }.apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        setSupportActionBar(toolbar)
        supportActionBar?.title = (intent.extras?.getString(BUNDLE_TITLE)) ?: UNKNOWN
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressBar?.visibility = View.VISIBLE
        (intent.extras?.getString(BUNDLE_URL))?.let {
            loadWebViewContent(it)
        }
    }

    private fun loadWebViewContent(it: String): Unit? {
        webView?.webChromeClient = WebChromeClient()
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar?.visibility = View.GONE
            }
        }
        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.pluginState = PluginState.ON
        webView?.settings?.defaultFontSize = 18
        return webView?.loadUrl(it)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
