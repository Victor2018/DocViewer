package com.cherry.lib.doc.widget

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cherry.lib.doc.R
import com.cherry.lib.doc.bean.DocSourceType
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.office.IOffice
import com.cherry.lib.doc.pdf.PdfDownloader
import com.cherry.lib.doc.pdf.PdfQuality
import com.cherry.lib.doc.pdf.PdfRendererCore
import com.cherry.lib.doc.pdf.PdfViewAdapter
import com.cherry.lib.doc.util.Constant
import com.cherry.lib.doc.util.FileUtils
import com.cherry.lib.doc.util.ViewUtils.hide
import com.cherry.lib.doc.util.ViewUtils.show
import kotlinx.android.synthetic.main.doc_rendererview.view.*
import kotlinx.coroutines.CoroutineScope
import java.io.File

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PdfView
 * Author: Victor
 * Date: 2023/10/30 11:30
 * Description: 
 * -----------------------------------------------------------------
 */

class DocView : FrameLayout {

    private val TAG = "PdfRendererView"

    var mActivity: Activity? = null
    private var pdfRendererCore: PdfRendererCore? = null
    private var pdfViewAdapter: PdfViewAdapter? = null
    private var quality = PdfQuality.NORMAL
    private var showDivider = true
    private var showPageNum = true
    private var divider: Drawable? = null
    private var runnable = Runnable {}
    var enableLoadingForPages: Boolean = true
    var pbDefaultHeight = 2
    var pbHeight: Int = pbDefaultHeight

    private var pdfRendererCoreInitialised = false
    var pageMargin: Rect = Rect(0,0,0,0)
    var statusListener: StatusCallBack? = null

    val totalPageCount = pdfRendererCore?.getPageCount() ?: 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(attrs, defStyle)
    }

    fun initView(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.doc_rendererview, this)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DocRendererView, defStyle, 0)
        val ratio =
            typedArray.getInt(R.styleable.DocRendererView_pdfView_quality, PdfQuality.NORMAL.ratio)
        quality = PdfQuality.values().first { it.ratio == ratio }
        showDivider = typedArray.getBoolean(R.styleable.DocRendererView_pdfView_showDivider, true)
        showPageNum = typedArray.getBoolean(R.styleable.DocRendererView_pdfView_show_page_num, true)
        divider = typedArray.getDrawable(R.styleable.DocRendererView_pdfView_divider)
        enableLoadingForPages = typedArray.getBoolean(R.styleable.DocRendererView_pdfView_enableLoadingForPages, enableLoadingForPages)
        pbHeight = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_pb_height, pbDefaultHeight)

        val marginDim = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_margin, 0)
        pageMargin = Rect(marginDim, marginDim, marginDim, marginDim).apply {
            top = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_marginTop, top)
            left = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_marginLeft, left)
            right = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_marginRight, right)
            bottom = typedArray.getDimensionPixelSize(R.styleable.DocRendererView_pdfView_page_marginBottom, bottom)
        }

        var layoutParams = mPlLoadProgress.layoutParams
        layoutParams.height = pbHeight
        mPlLoadProgress.layoutParams = layoutParams

        typedArray.recycle()


        runnable = Runnable {
            mPdfPageNo.hide()
        }
    }

    fun openDoc(activity: Activity, docUrl: String?, docSourceType: Int) {
        mActivity = activity
        if (docSourceType == DocSourceType.URL) {
            downloadFile(docUrl ?: "")
            return
        }

        openDoc(activity, docUrl, docSourceType,-1)
    }

    fun openDoc(activity: Activity?, docUrl: String?, docSourceType: Int, fileType: Int) {
        mActivity = activity
        Log.e(TAG,"openDoc()......fileType = $fileType")
        if (docSourceType == DocSourceType.URL) {
            downloadFile(docUrl ?: "")
            return
        }

        var type = FileUtils.getFileTypeForUrl(docUrl)
        if (fileType > 0) {
            type = fileType
        }
        when (type) {
            FileType.PDF -> {
                if (showPageNum) {
                    showPageNum = false
                }
                Log.e(TAG,"openDoc()......PDF")
                mPWeb.hide()
                mFlDocContainer.hide()
                mRvPdf.show()
                mIvImage.hide()

                showPdf(docSourceType,docUrl)
            }
            FileType.IMAGE -> {
                if (showPageNum) {
                    showPageNum = false
                }
                Log.e(TAG,"openDoc()......")
                mPWeb.hide()
                mFlDocContainer.hide()
                mRvPdf.hide()
                mIvImage.show()
                if (docSourceType == DocSourceType.PATH) {
                    Log.e(TAG,"openDoc()......PATH")
                    mIvImage.load(File(docUrl))
                } else {
                    Log.e(TAG,"openDoc()......URL")
                    mIvImage.load(docUrl)
                }
            }
            FileType.NOT_SUPPORT -> {
                if (showPageNum) {
                    showPageNum = false
                }
                Log.e(TAG,"openDoc()......NOT_SUPPORT")
                mPWeb.show()
                mFlDocContainer.hide()
                mRvPdf.hide()
                mIvImage.hide()
                mPWeb.loadUrl(Constant.XDOC_VIEW_URL + docUrl)
            }
            else -> {
                Log.e(TAG,"openDoc()......ELSE")
                mPWeb.hide()
                mFlDocContainer.show()
                mRvPdf.hide()
                mIvImage.hide()
                activity?.let { showDoc(it,mFlDocContainer,docUrl,docSourceType,fileType) }
            }
        }
    }

    fun showDoc(activity: Activity, mDocContainer: ViewGroup?, url: String?, docSourceType: Int, fileType: Int) {
        Log.e(TAG,"showDoc()......")
        var iOffice: IOffice = object: IOffice() {
            override fun getActivity(): Activity {
                return activity
            }

            override fun openFileFinish() {
                mDocContainer?.postDelayed({
                    mDocContainer.removeAllViews()
                    mDocContainer.addView(
                        view,
                        RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                },200)
            }

            override fun getAppName(): String {
                return "Loading..."
            }

            override fun getTemporaryDirectory(): File {
                val file = activity.getExternalFilesDir(null)
                return file ?: activity.filesDir
            }

            override fun fullScreen(fullscreen: Boolean) {
            }

        }
        iOffice.openFile(url,docSourceType, fileType.toString())
    }

    fun showPdf(docSourceType: Int, url: String?) {
        Log.e(TAG,"showPdf()......")
        when (docSourceType) {
            DocSourceType.URL -> {
                Log.e(TAG,"showPdf()......URL")
                initWithUrl(url = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.URI -> {
                Log.e(TAG,"showPdf()......URI")
                initWithUri(fileUri = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.PATH -> {
                Log.e(TAG,"showPdf()......PATH")
                initWithPath(path = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.ASSETS -> {
                Log.e(TAG,"showPdf()......ASSETS")
                initWithAssets(fileName = url ?: "", pdfQuality = PdfQuality.FAST)
            }
        }
    }

    private fun showPdf(file: File, pdfQuality: PdfQuality) {
        Log.e(javaClass.simpleName,"initView-exists = ${file.exists()}")
        pdfRendererCore = PdfRendererCore(context, file, pdfQuality)
        pdfRendererCoreInitialised = true
        pdfViewAdapter = PdfViewAdapter(pdfRendererCore, pageMargin, enableLoadingForPages)

        mRvPdf.adapter = pdfViewAdapter
        mRvPdf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvPdf.itemAnimator = DefaultItemAnimator()
        mRvPdf.addOnScrollListener(scrollListener)

        if (showDivider) {
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                divider?.let { setDrawable(it) }
            }.let { mRvPdf.addItemDecoration(it) }
        }
    }

    fun initWithUrl(
        url: String,
        pdfQuality: PdfQuality = this.quality,
        lifecycleScope: LifecycleCoroutineScope = (context as AppCompatActivity).lifecycleScope
    ) {
        downloadFile(url, pdfQuality, lifecycleScope)
    }

    fun initWithPath(path: String, pdfQuality: PdfQuality = this.quality) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            throw UnsupportedOperationException("should be over API 21")
        initWithFile(File(path), pdfQuality)
    }

    fun initWithFile(file: File, pdfQuality: PdfQuality = this.quality) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            throw UnsupportedOperationException("should be over API 21")
        showPdf(file, pdfQuality)
    }

    fun initWithAssets(fileName: String, pdfQuality: PdfQuality = this.quality) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            throw UnsupportedOperationException("should be over API 21")

        val file = FileUtils.fileFromAsset(context,fileName)
        showPdf(file, pdfQuality)
    }

    fun initWithUri(fileUri: String, pdfQuality: PdfQuality = this.quality) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            throw UnsupportedOperationException("should be over API 21")

        val file = FileUtils.fileFromUri(context,fileUri)
        showPdf(file, pdfQuality)
    }

    fun downloadFile(url: String, pdfQuality: PdfQuality = this.quality,
                     lifecycleScope: LifecycleCoroutineScope = (context as AppCompatActivity).lifecycleScope) {
        PdfDownloader(url, object : PdfDownloader.StatusListener {
            override fun getContext(): Context = context
            override fun onDownloadStart() {
                Log.e(TAG,"initWithUrl-onDownloadStart()......")
                statusListener?.onDownloadStart()
            }

            override fun onDownloadProgress(currentBytes: Long, totalBytes: Long) {
                var progress = (currentBytes.toFloat() / totalBytes.toFloat() * 100F).toInt()
                if (progress >= 100) {
                    progress = 100
                }
                Log.e(TAG,"initWithUrl-onDownloadProgress()......progress = $progress")
                statusListener?.onDownloadProgress(progress, currentBytes, totalBytes)

                mPlLoadProgress?.show()
                mPlLoadProgress?.progress = progress
            }

            override fun onDownloadSuccess(absolutePath: String) {
                Log.e(TAG,"initWithUrl-onDownloadSuccess()......")
                openDoc(mActivity, absolutePath, DocSourceType.PATH,-1)

                statusListener?.onDownloadSuccess()
                mPlLoadProgress?.hide()
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
                Log.e(TAG,"initWithUrl-onError()......${error.localizedMessage}")
                statusListener?.onError(error)
                mPlLoadProgress?.hide()
            }

            override fun getCoroutineScope(): CoroutineScope = lifecycleScope
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView.layoutManager as LinearLayoutManager).run {
                var foundPosition : Int = findLastCompletelyVisibleItemPosition()

                if (foundPosition != RecyclerView.NO_POSITION)
                    mPdfPageNo.text = context.getString(R.string.pdfView_page_no,foundPosition + 1,totalPageCount)

                if (showPageNum) {
                    mPdfPageNo.visibility = View.VISIBLE
                }

                if (foundPosition == 0)
                    mPdfPageNo.postDelayed({
                        mPdfPageNo.visibility = GONE
                    }, 3000)

                if (foundPosition != RecyclerView.NO_POSITION) {
                    statusListener?.onPageChanged(foundPosition, totalPageCount)
                    return@run
                }
                foundPosition = findFirstVisibleItemPosition()
                if (foundPosition != RecyclerView.NO_POSITION) {
                    statusListener?.onPageChanged(foundPosition, totalPageCount)
                    return@run
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mPdfPageNo.postDelayed(runnable, 3000)
            } else {
                mPdfPageNo.removeCallbacks(runnable)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        closePdfRender()
    }

    fun closePdfRender() {
        if (pdfRendererCoreInitialised) {
            pdfRendererCore?.closePdfRender()
        }
    }


    interface StatusCallBack {
        fun onDownloadStart() {}
        fun onDownloadProgress(progress: Int, downloadedBytes: Long, totalBytes: Long?) {}
        fun onDownloadSuccess() {}
        fun onError(error: Throwable) {}
        fun onPageChanged(currentPage: Int, totalPage: Int) {}
    }
}