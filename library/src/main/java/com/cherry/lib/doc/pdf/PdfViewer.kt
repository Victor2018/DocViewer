package com.cherry.lib.doc.pdf

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
        when (docSourceType) {
            DocSourceType.URL -> {
                pdfView?.initWithUrl(url = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.URI -> {
                pdfView?.initWithUri(fileUri = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.PATH -> {
                pdfView?.initWithPath(path = url ?: "", pdfQuality = PdfQuality.FAST)
            }
            DocSourceType.ASSETS -> {
                pdfView?.initWithAssets(fileName = url ?: "", pdfQuality = PdfQuality.FAST)
            }
        }
    }
}