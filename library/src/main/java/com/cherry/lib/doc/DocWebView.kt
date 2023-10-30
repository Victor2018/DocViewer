package com.cherry.lib.doc

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ProgressWebView
 * Author: Victor
 * Date: 2022/3/1 18:28
 * Description: 
 * -----------------------------------------------------------------
 */

class DocWebView constructor(context: Context, attributeset: AttributeSet) : ConstraintLayout(context, attributeset),
    DownloadListener {

    val TAG = "DocWebView"
    var progressBar: ProgressBar
    var webview: WebView
    var isLastLoadSuccess = false//是否成功加载完成过web，成功过后的网络异常 不改变web
    var isError = false

    var openLinkBySysBrowser = false//是否使用系统浏览器打开http链接

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.web_progress_view, this, true)
        progressBar = rootView.findViewById(R.id.pb_webview)
        webview = rootView.findViewById(R.id.web_view)
        webview.webChromeClient = MyWebChromeClient()
        webview.webViewClient = MyWebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.allowFileAccess = true
        webview.settings.allowFileAccessFromFileURLs =true
        webview.settings.allowUniversalAccessFromFileURLs = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.setDownloadListener(this)

    }

    private inner class MyWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            setProgress(newProgress)
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if (title.contains("html")) {
                return
            }
            listener?.onTitle(title)
        }
    }

    private fun setProgress(newProgress: Int) {
        if (newProgress == 100) {
            progressBar.visibility = View.GONE
        } else {
            progressBar.progress = newProgress
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            //在访问失败的时候会首先回调onReceivedError，然后再回调onPageFinished。
            if (!isError) {
                isLastLoadSuccess = true
                listener?.success()
            }
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            //在访问失败的时候会首先回调onReceivedError，然后再回调onPageFinished。
            isError = true
            if (!isLastLoadSuccess) {//之前成功加载完成过，不会回调
                listener?.error()
            }
        }
    }

    /**
     * 千万不要更改这个 "SSDJsBirdge"  注意！！！！！
     */
    @SuppressLint("JavascriptInterface")
    fun addJavascriptInterface(jsInterface: Any) {
        webview.addJavascriptInterface(jsInterface, "SSDJsBirdge")
    }

    fun reload() {
        isError = false
        webview.reload()
    }

    fun loadUrl(url: String) {
        isError = false
        try {
            webview.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadData (htmlData: String) {
        webview.loadData(htmlData,"text/html","utf-8")
    }

    fun loadData (htmlData: String,secondLinkBySysBrowser: Boolean) {
        openLinkBySysBrowser = secondLinkBySysBrowser
        webview.loadData(htmlData,"text/html","utf-8")
    }

    fun downloadFile(url: String?,contentDisposition: String?,mimeType: String?) {
        val request = DownloadManager.Request(Uri.parse(url))
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner()
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // 设置通知栏的标题，如果不设置，默认使用文件名
        request.setTitle("下载完成")
        // 设置通知栏的描述
//                    request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(true)
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true)
        // 允许漫游时下载
        request.setAllowedOverRoaming(true)

        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
        Log.e(TAG, "downloadFile()-fileName = $fileName")
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().toString() + "/Download/", fileName)


        val downloadManager = webview.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 添加一个下载任务
        val downloadId = downloadManager.enqueue(request)
    }

    override fun onDownloadStart(url: String?, userAgent: String?, contentDisposition: String?, mimeType: String?, contentLength: Long) {
        Log.e(TAG,"onDownloadStart()......url = $url")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)

//        downloadFile(url,contentDisposition,mimeType)
    }

    fun canGoBack(): Boolean {
        val canGoBack = webview.canGoBack()
        if (canGoBack) {
            webview.goBack()
        }
        return canGoBack
    }

    fun onPause () {
        webview.pauseTimers()

    }

    fun onResume () {
        webview.resumeTimers()
    }

    /**
     * must be called on the main thread
     */
    fun onDestory() {
        try {
            webview.clearHistory();
            webview.clearCache(true)
            webview.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webview.freeMemory()
            webview.pauseTimers()
            webview.destroy() // Note that mWebView.destroy() and mWebView = null do the exact same thing
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setWebViewBackgroundColor(isBlack: Boolean) {
        if (isBlack) {
            //防止加载html白屏(针对播放视频)
            setBackgroundColor(Color.BLACK)
        }
    }

    private var listener: OnWebLoadStatusListener? = null

    fun setOnLoadStatueListener(listener: OnWebLoadStatusListener) {
        this.listener = listener
    }

    interface OnWebLoadStatusListener {
        fun error()

        fun success()

        fun onTitle(title: String)
    }
}