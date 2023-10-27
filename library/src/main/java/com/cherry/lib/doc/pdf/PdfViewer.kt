package com.cherry.lib.doc.pdf

import android.util.Log
import com.cherry.lib.doc.DocViewer
import com.cherry.lib.doc.bean.DocSourceType

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PdfViewer
 * Author: Victor
 * Date: 2023/09/28 17:03
 * Description: 
 * -----------------------------------------------------------------
 */

object PdfViewer {

    fun showPdf(docSourceType: Int,pdfView: PdfRendererView?,url: String?) {
        Log.e(DocViewer.TAG,"showPdf()......")
        when (docSourceType) {
            DocSourceType.URL -> {
                Log.e(DocViewer.TAG,"showPdf()......URL")
                pdfView?.initWithUrl(url = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.URI -> {
                Log.e(DocViewer.TAG,"showPdf()......URI")
                pdfView?.initWithUri(fileUri = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.PATH -> {
                Log.e(DocViewer.TAG,"showPdf()......PATH")
                pdfView?.initWithPath(path = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.ASSETS -> {
                Log.e(DocViewer.TAG,"showPdf()......ASSETS")
                pdfView?.initWithAssets(fileName = url ?: "", pdfQuality = PdfQuality.FAST)
            }
        }
    }
}