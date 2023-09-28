package com.cherry.lib.doc

import com.cherry.lib.doc.pdf.PdfRendererView
import com.cherry.lib.doc.pdf.PdfViewer

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DocViewer
 * Author: Victor
 * Date: 2023/09/28 10:49
 * Description: 
 * -----------------------------------------------------------------
 */

object DocViewer {
    fun showPdf(docSourceType: Int, pdfView: PdfRendererView?, url: String?) {
        PdfViewer.showPdf(docSourceType, pdfView, url)
    }
}