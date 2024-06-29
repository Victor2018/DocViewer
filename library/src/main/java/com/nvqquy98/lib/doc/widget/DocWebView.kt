package com.nvqquy98.lib.doc.widget

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
import android.webkit.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.nvqquy98.lib.doc.R
import com.nvqquy98.lib.doc.interfaces.OnWebLoadListener
import kotlinx.android.synthetic.main.doc_web_view.view.*
import timber.log.Timber

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

class DocWebView : ConstraintLayout, DownloadListener {
    val TAG = "DocWebView"
    var isLastLoadSuccess = false//是否成功加载完成过web，成功过后的网络异常 不改变web
    var isError = false
    var openLinkBySysBrowser = false//是否使用系统浏览器打开http链接
    var mOnWebLoadListener: OnWebLoadListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    fun initView() {
        LayoutInflater.from(context).inflate(R.layout.doc_web_view, this, true)
        mDocView.webChromeClient = DocWebChromeClient()
        mDocView.webViewClient = DocWebViewClient()
        //设置可以支持缩放
        mDocView.settings.setSupportZoom(true)
        //设置出现缩放工具
        mDocView.settings.builtInZoomControls = true
        //设定缩放控件隐藏
        mDocView.settings.displayZoomControls = true
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        mDocView.settings.useWideViewPort = true
        //设置默认加载的可视范围是大视野范围
        mDocView.settings.loadWithOverviewMode = true
        //自适应屏幕 SINGLE_COLUMN：把所有内容放大到webview等宽的一列中 NORMAL：正常显示不做任何渲染。NARROW_COLUMNS：可能的话让所有列的宽度不超过屏幕宽度
        mDocView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        mDocView.settings.javaScriptEnabled = true
        mDocView.settings.domStorageEnabled = true
        mDocView.settings.allowFileAccess = true
        mDocView.settings.allowFileAccessFromFileURLs = true
        mDocView.settings.allowUniversalAccessFromFileURLs = true
        mDocView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        mDocView.setDownloadListener(this)
    }

    private fun setProgress(newProgress: Int) {
        mOnWebLoadListener?.OnWebLoadProgress(newProgress)
    }

    /**
     * 千万不要更改这个 "SSDJsBirdge"  注意！！！！！
     */
    @SuppressLint("JavascriptInterface")
    fun addJavascriptInterface(jsInterface: Any) {
        mDocView.addJavascriptInterface(jsInterface, "SSDJsBirdge")
    }

    fun reload() {
        isError = false
        mDocView.reload()
    }

    fun loadUrl(url: String) {
        isError = false
        try {
            mDocView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadData(htmlData: String) {
        mDocView.loadData(htmlData, "text/html", "utf-8")
    }

    fun loadData(htmlData: String, secondLinkBySysBrowser: Boolean) {
        openLinkBySysBrowser = secondLinkBySysBrowser
        mDocView.loadData(htmlData, "text/html", "utf-8")
    }

    fun downloadFile(url: String?, contentDisposition: String?, mimeType: String?) {
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
        Timber.e("downloadFile()-fileName = $fileName")
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().toString() + "/Download/", fileName)
        val downloadManager = mDocView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 添加一个下载任务
        val downloadId = downloadManager.enqueue(request)
    }

    override fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimeType: String?,
        contentLength: Long
    ) {
        Timber.e("onDownloadStart()......url = $url")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
//        downloadFile(url,contentDisposition,mimeType)
    }

    fun canGoBack(): Boolean {
        val canGoBack = mDocView.canGoBack()
        if (canGoBack) {
            mDocView.goBack()
        }
        return canGoBack
    }

    fun onPause() {
        mDocView.pauseTimers()
    }

    fun onResume() {
        mDocView.resumeTimers()
    }

    /**
     * must be called on the main thread
     */
    fun onDestroy() {
        try {
            mDocView.clearHistory();
            mDocView.clearCache(true)
            mDocView.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mDocView.freeMemory()
            mDocView.pauseTimers()
            mDocView.destroy() // Note that mWebView.destroy() and mWebView = null do the exact same thing
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    private inner class DocWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            setProgress(newProgress)
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if (title.contains("html")) {
                return
            }
            mOnWebLoadListener?.onTitle(title)
        }
    }

    private inner class DocWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            //在访问失败的时候会首先回调onReceivedError，然后再回调onPageFinished。
            if (!isError) {
                isLastLoadSuccess = true
                mOnWebLoadListener?.OnWebLoadProgress(100)
            }
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            //在访问失败的时候会首先回调onReceivedError，然后再回调onPageFinished。
            isError = true
            if (!isLastLoadSuccess) {//之前成功加载完成过，不会回调
                mOnWebLoadListener?.OnWebLoadProgress(100)
            }
        }
    }
}
