package com.nvqquy98.lib.doc.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.blankj.utilcode.util.UriUtils
import com.nvqquy98.lib.doc.R
import com.nvqquy98.lib.doc.bean.DocEngine
import com.nvqquy98.lib.doc.bean.DocMovingOrientation
import com.nvqquy98.lib.doc.bean.DocSourceType
import com.nvqquy98.lib.doc.bean.FileType
import com.nvqquy98.lib.doc.interfaces.OnDownloadListener
import com.nvqquy98.lib.doc.interfaces.OnDocPageChangeListener
import com.nvqquy98.lib.doc.interfaces.OnPdfItemClickListener
import com.nvqquy98.lib.doc.interfaces.OnWebLoadListener
import com.nvqquy98.lib.doc.office.IOffice
import com.nvqquy98.lib.doc.pdf.PdfDownloader
import com.nvqquy98.lib.doc.pdf.PdfPageViewAdapter
import com.nvqquy98.lib.doc.pdf.PdfQuality
import com.nvqquy98.lib.doc.pdf.PdfRendererCore
import com.nvqquy98.lib.doc.pdf.PdfViewAdapter
import com.nvqquy98.lib.doc.util.Constant
import com.nvqquy98.lib.doc.util.FileUtils
import com.nvqquy98.lib.doc.util.ViewUtils.hide
import com.nvqquy98.lib.doc.util.ViewUtils.show
import kotlinx.android.synthetic.main.doc_view.view.*
import timber.log.Timber
import java.io.File
import java.net.URLEncoder

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

class DocView : FrameLayout,OnDownloadListener, OnWebLoadListener,OnPdfItemClickListener {

    private val TAG = "DocView"

    var mActivity: Activity? = null
    var lifecycleScope: LifecycleCoroutineScope = (context as AppCompatActivity).lifecycleScope
    private var mPoiViewer: PoiViewer? = null
    private var pdfRendererCore: PdfRendererCore? = null
    private var pdfViewAdapter: PdfViewAdapter? = null
    private var pdfPageViewAdapter: PdfPageViewAdapter? = null
    private var mMovingOrientation = DocMovingOrientation.HORIZONTAL
    private var quality = PdfQuality.NORMAL
    private var engine = DocEngine.INTERNAL
    private var showDivider = true
    private var showPageNum = true
    private var divider: Drawable? = null
    private var runnable = Runnable {}
    var enableLoadingForPages: Boolean = true
    var pbDefaultHeight = 2
    var pbHeight: Int = pbDefaultHeight
    var pbDefaultColor = Color.RED
    var pbColor: Int = pbDefaultColor

    private var pdfRendererCoreInitialised = false
    var pageMargin: Rect = Rect(0,0,0,0)

    var totalPageCount = 0

    var mOnDocPageChangeListener: OnDocPageChangeListener? = null

    var sourceFilePath: String? = null
    var mViewPdfInPage: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(attrs, defStyle)
    }

    fun initView(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.doc_view, this)

        mIvPdf.setOnClickListener {
            mLlBigPdfImage.hide()
        }

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DocView, defStyle, 0)
        val orientation =
            typedArray.getInt(R.styleable.DocView_dv_moving_orientation, DocMovingOrientation.HORIZONTAL.orientation)
        mMovingOrientation = DocMovingOrientation.values().first { it.orientation == orientation }
        val ratio =
            typedArray.getInt(R.styleable.DocView_dv_quality, PdfQuality.NORMAL.ratio)
        quality = PdfQuality.values().first { it.ratio == ratio }
        val engineValue =
            typedArray.getInt(R.styleable.DocView_dv_engine, DocEngine.INTERNAL.value)
        engine = DocEngine.values().first { it.value == engineValue }
        showDivider = typedArray.getBoolean(R.styleable.DocView_dv_showDivider, true)
        showPageNum = typedArray.getBoolean(R.styleable.DocView_dv_show_page_num, true)
        divider = typedArray.getDrawable(R.styleable.DocView_dv_divider)
        enableLoadingForPages = typedArray.getBoolean(R.styleable.DocView_dv_enableLoadingForPages, enableLoadingForPages)
        pbHeight = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_pb_height, pbDefaultHeight)
        pbColor = typedArray.getColor(R.styleable.DocView_dv_page_pb_color, pbDefaultColor)

        val marginDim = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_margin, 0)
        pageMargin = Rect(marginDim, marginDim, marginDim, marginDim).apply {
            top = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_marginTop, top)
            left = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_marginLeft, left)
            right = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_marginRight, right)
            bottom = typedArray.getDimensionPixelSize(R.styleable.DocView_dv_page_marginBottom, bottom)
        }

        var layoutParams = mPlLoadProgress.layoutParams
        layoutParams.height = pbHeight
        mPlLoadProgress.layoutParams = layoutParams

        mPlLoadProgress.progressTintList = ColorStateList.valueOf(pbColor)

        typedArray.recycle()


        runnable = Runnable {
            mPdfPageNo.hide()
        }
    }


    fun openDoc(activity: Activity, docUrl: String?, docSourceType: Int,
                engine: DocEngine = this.engine) {
        mActivity = activity
        openDoc(activity, docUrl, docSourceType,-1,false,engine)
    }

    fun openDoc(activity: Activity?, docUrl: String?,
                docSourceType: Int, fileType: Int,
                viewPdfInPage: Boolean = false,
                engine: DocEngine = this.engine) {
        var fileType = fileType
        var docUrl = docUrl
        var docSourceType = docSourceType
        if (docUrl != null && docSourceType == DocSourceType.URI && fileType == -1) {
            // 如果是URI类型，且文件类型为-1，则获取一下文件类型，保证正确读取
            val uri = docUrl.toUri()
            Timber.d(TAG, "openDoc reset uri = $uri")
            val file = UriUtils.uri2File(uri)
            if (file != null) {
                var mimeType = ""
                fileType = FileUtils.getFileTypeForUrl(file.absolutePath)
                if (fileType == FileType.NOT_SUPPORT) {
                    mimeType = FileUtils.getFileMimeType(context, uri) ?: "*/*"
                    fileType = FileUtils.getFileTypeForUrl(FileUtils.mimeExtMap[mimeType] ?: "")
                }
                docUrl = file.absolutePath
                docSourceType = DocSourceType.PATH
                Timber.d(TAG, "openDoc reset url = $docUrl")
                Timber.d(TAG, "openDoc reset docSourceType = $docSourceType")
                Timber.d(TAG, "openDoc reset fileType = $fileType, mimeType = $mimeType")
            } else {
                Timber.d(TAG, "file = null")
            }
        }
        Timber.e(TAG,"openDoc()......fileType = $fileType")
        mActivity = activity
        mViewPdfInPage = viewPdfInPage
        if (docSourceType == DocSourceType.PATH) {
            sourceFilePath = docUrl
        } else {
            sourceFilePath = null
        }
        if (docSourceType == DocSourceType.URL && fileType != FileType.IMAGE) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
                || engine == DocEngine.MICROSOFT
                || engine == DocEngine.XDOC
                || engine == DocEngine.GOOGLE) {
                showByWeb(docUrl ?: "",engine)
                return
            }
            downloadFile(docUrl ?: "")
            return
        }

        var type = FileUtils.getFileTypeForUrl(docUrl)
        if (fileType > 0) {
            type = fileType
        }
        when (type) {
            FileType.PDF -> {
                Timber.e(TAG,"openDoc()......PDF")
                mDocWeb.hide()
                mFlDocContainer.hide()
                mRvPdf.show()
                mIvImage.hide()

                showPdf(docSourceType,docUrl)
            }
            FileType.IMAGE -> {
                if (showPageNum) {
                    showPageNum = false
                }
                Timber.e(TAG,"openDoc()......")
                mDocWeb.hide()
                mFlDocContainer.hide()
                mRvPdf.hide()
                mIvImage.show()
                if (docSourceType == DocSourceType.PATH) {
                    Timber.e(TAG,"openDoc()......PATH")
                    mIvImage.load(File(docUrl))
                } else {
                    Timber.e(TAG,"openDoc()......URL")
                    mIvImage.load(docUrl)
                }
            }
            FileType.NOT_SUPPORT -> {
                if (showPageNum) {
                    showPageNum = false
                }
                Timber.e(TAG,"openDoc()......NOT_SUPPORT")
                mDocWeb.show()
                mFlDocContainer.hide()
                mRvPdf.hide()
                mIvImage.hide()
                showByWeb(docUrl ?: "",this.engine)
            }
            else -> {
                Timber.e(TAG,"openDoc()......ELSE")
                if (showPageNum) {
                    showPageNum = false
                }
                mDocWeb.hide()
                mFlDocContainer.show()
                mRvPdf.hide()
                mIvImage.hide()
                activity?.let { showDoc(it,mFlDocContainer,docUrl,docSourceType,fileType) }
            }
        }
    }

    fun showDoc(activity: Activity, mDocContainer: ViewGroup?, url: String?, docSourceType: Int, fileType: Int) {
        Timber.e(TAG,"showDoc()......")
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

            override fun openFileFailed() {
                try {
                    if (mPoiViewer == null) {
                        mPoiViewer = PoiViewer(context)
                    }
                    mPoiViewer?.loadFile(mFlDocContainer, sourceFilePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, R.string.open_failed, Toast.LENGTH_SHORT).show()
                }
            }

            override fun getAppName(): String {
                return context.resources.getString(R.string.loading)
            }

            override fun getTemporaryDirectory(): File {
                val file = activity.getExternalFilesDir(null)
                return file ?: activity.filesDir
            }

            override fun fullScreen(fullscreen: Boolean) {
            }

            override fun getMovingOrientation(): Int {
                return mMovingOrientation.orientation
            }

        }
        iOffice.openFile(url,docSourceType, fileType.toString())
    }

    fun showPdf(docSourceType: Int, url: String?) {
        Timber.e(TAG,"showPdf()......quality = $quality")
        when (docSourceType) {
            DocSourceType.URL -> {
                Timber.e(TAG,"showPdf()......URL")
                initWithUrl(url = url ?: "", pdfQuality = quality)
            }
            DocSourceType.URI -> {
                Timber.e(TAG,"showPdf()......URI")
                initWithUri(fileUri = url ?: "", pdfQuality = quality)
            }
            DocSourceType.PATH -> {
                Timber.e(TAG,"showPdf()......PATH")
                initWithPath(path = url ?: "", pdfQuality = quality)
            }
            DocSourceType.ASSETS -> {
                Timber.e(TAG,"showPdf()......ASSETS")
                initWithAssets(fileName = url ?: "", pdfQuality = quality)
            }
        }
    }

    private fun showPdf(file: File, pdfQuality: PdfQuality) {
        Timber.e(javaClass.simpleName,"initView-exists = ${file.exists()}")
        Timber.e(javaClass.simpleName,"initView-mViewPdfInPage = $mViewPdfInPage")
        pdfRendererCore = PdfRendererCore(context, file, pdfQuality)
        totalPageCount = pdfRendererCore?.getPageCount() ?: 0
        pdfRendererCoreInitialised = true
        pdfViewAdapter = PdfViewAdapter(pdfRendererCore, pageMargin, enableLoadingForPages,this)
        pdfPageViewAdapter = PdfPageViewAdapter(pdfRendererCore, pageMargin, enableLoadingForPages)
        mRvPdf.setEnableScale(true)
        if (mViewPdfInPage) {
            mRvPdf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mRvPdf.adapter = pdfPageViewAdapter
            PagerSnapHelper().attachToRecyclerView(mRvPdf)
        } else {
            mRvPdf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mRvPdf.adapter = pdfViewAdapter
        }
        mRvPdf.itemAnimator = DefaultItemAnimator()
        mRvPdf.addOnScrollListener(scrollListener)

        if (showDivider && !mViewPdfInPage) {
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                divider?.let { setDrawable(it) }
            }.let { mRvPdf.addItemDecoration(it) }
        }
    }

    fun initWithUrl(
        url: String,
        pdfQuality: PdfQuality = this.quality,
        engine: DocEngine = this.engine,
        lifecycleScope: LifecycleCoroutineScope = (context as AppCompatActivity).lifecycleScope
    ) {
        this.lifecycleScope = lifecycleScope
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
        PdfDownloader(url, this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showByWeb(url: String,engine: DocEngine = this.engine) {
        mDocWeb.mOnWebLoadListener = this

        var engineUrl = "engine"
        when (engine) {
            DocEngine.MICROSOFT -> {
                engineUrl = Constant.MICROSOFT_URL
            }
            DocEngine.XDOC -> {
                engineUrl = Constant.XDOC_VIEW_URL
            }
            DocEngine.GOOGLE -> {
                engineUrl = Constant.GOOGLE_URL
            }
            else -> {
                engineUrl = Constant.XDOC_VIEW_URL
            }
        }
        mDocWeb.loadUrl("$engineUrl${URLEncoder.encode(url, "UTF-8")}")
    }

    override fun getDownloadContext() = context

    override fun onDownloadStart() {
        Timber.e(TAG,"initWithUrl-onDownloadStart()......")
    }

    override fun onDownloadProgress(currentBytes: Long, totalBytes: Long) {
        var progress = (currentBytes.toFloat() / totalBytes.toFloat() * 100F).toInt()
        if (progress >= 100) {
            progress = 100
        }
        Timber.e(TAG,"initWithUrl-onDownloadProgress()......progress = $progress")
        showLoadingProgress(progress)
    }

    override fun onDownloadSuccess(absolutePath: String) {
        Timber.e(TAG,"initWithUrl-onDownloadSuccess()......")
        showLoadingProgress(100)
        sourceFilePath = absolutePath
        openDoc(mActivity, absolutePath, DocSourceType.PATH,-1,mViewPdfInPage)
    }

    override fun onError(error: Throwable) {
        error.printStackTrace()
        Timber.e(TAG,"initWithUrl-onError()......${error.localizedMessage}")
        showLoadingProgress(100)
    }

    override fun getCoroutineScope() = lifecycleScope

    override fun OnWebLoadProgress(progress: Int) {
        showLoadingProgress(progress)
    }

    override fun onTitle(title: String?) {
    }

    fun showLoadingProgress(progress: Int) {
        if (progress == 100) {
            mPlLoadProgress.hide()
        } else {
            mPlLoadProgress?.show()
            mPlLoadProgress?.progress = progress
        }
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

                if (foundPosition == 0 && !mViewPdfInPage)
                    mPdfPageNo.postDelayed({
                        mPdfPageNo.visibility = GONE
                    }, 3000)

                if (foundPosition != RecyclerView.NO_POSITION) {
                    mOnDocPageChangeListener?.OnPageChanged(foundPosition, totalPageCount)
                    return@run
                }
                foundPosition = findFirstVisibleItemPosition()
                if (foundPosition != RecyclerView.NO_POSITION) {
                    mOnDocPageChangeListener?.OnPageChanged(foundPosition, totalPageCount)
                    return@run
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE  && !mViewPdfInPage) {
                mPdfPageNo.postDelayed(runnable, 3000)
            } else {
                mPdfPageNo.removeCallbacks(runnable)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    fun closePdfRender() {
        try {
            if (pdfRendererCoreInitialised) {
                pdfRendererCore?.closePdfRender()
                pdfRendererCoreInitialised = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun OnPdfItemClick(position: Int) {
        mLlBigPdfImage.show()
        mPbBigLoading.show()
        mIvPdf.setImageBitmap(null)
        pdfRendererCore?.renderPage(position, PdfQuality.ENHANCED) { bitmap: Bitmap?, pageNo: Int ->
            mPbBigLoading.hide()
            mIvPdf.setImageBitmap(bitmap)
            mIvPdf.reset()
            mPdfPageNo.visibility = GONE
        }
    }

    fun onDestroy() {
        mPoiViewer?.recycle()
        closePdfRender()
        mOnDocPageChangeListener = null
    }
}
