package com.cherry.lib.doc.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import coil.load
import com.cherry.lib.doc.DocViewer
import com.cherry.lib.doc.R
import com.cherry.lib.doc.bean.DocSourceType
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.pdf.PdfRendererView
import com.cherry.lib.doc.util.Constant
import com.cherry.lib.doc.util.FileUtils
import com.cherry.lib.doc.util.ViewUtils.hide
import com.cherry.lib.doc.util.ViewUtils.show
import kotlinx.android.synthetic.main.doc_view_layout.view.*
import java.io.File

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DocView
 * Author: Victor
 * Date: 2023/10/27 16:05
 * Description: 
 * -----------------------------------------------------------------
 */

class DocView : FrameLayout {
    val TAG = "DocView"
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    fun initView() {
        inflate(context, R.layout.doc_view_layout, this)
    }

    fun openDoc(activity: Activity, docUrl: String?, docSourceType: Int) {
        openDoc(activity, docUrl, docSourceType,-1)
    }

    fun openDoc(activity: Activity, docUrl: String?, docSourceType: Int, fileType: Int) {
        Log.e(TAG,"openDoc()......fileType = $fileType")
        var type = FileUtils.getFileTypeForUrl(docUrl)
        if (fileType > 0) {
            type = fileType
        }
        when (type) {
            FileType.PDF -> {
                Log.e(TAG,"openDoc()......PDF")
                mPWeb.hide()
                mFlDocContainer.hide()
                mPdfView.show()
                mIvImage.hide()
                DocViewer.showPdf(docSourceType,mPdfView,docUrl)
            }
            FileType.IMAGE -> {
                Log.e(TAG,"openDoc()......")
                mPWeb.hide()
                mFlDocContainer.hide()
                mPdfView.hide()
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
                Log.e(TAG,"openDoc()......NOT_SUPPORT")
                mPWeb.show()
                mFlDocContainer.hide()
                mPdfView.hide()
                mIvImage.hide()
                mPWeb.loadUrl(Constant.XDOC_VIEW_URL + docUrl)
            }
            else -> {
                Log.e(TAG,"openDoc()......ELSE")
                mPWeb.hide()
                mFlDocContainer.show()
                mPdfView.hide()
                mIvImage.hide()
                DocViewer.showDoc(activity,mFlDocContainer,docUrl,docSourceType,fileType)
            }
        }
    }
    
    fun getPdfView(): PdfRendererView {
        return mPdfView
    }
}