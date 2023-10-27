package com.cherry.lib.doc

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.office.IOffice
import com.cherry.lib.doc.office.constant.MainConstant
import com.cherry.lib.doc.pdf.PdfRendererView
import com.cherry.lib.doc.pdf.PdfViewer
import java.io.File

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
    val TAG = "DocViewer"
    fun showPdf(docSourceType: Int, pdfView: PdfRendererView?, url: String?) {
        Log.e(TAG,"showPdf()......")
        PdfViewer.showPdf(docSourceType, pdfView, url)
    }

    fun showDoc(activity: Activity, mDocContainer: ViewGroup?, url: String?,docSourceType: Int,fileType: Int) {
        Log.e(TAG,"showDoc()......")
        var iOffice: IOffice = object: IOffice() {
            override fun getActivity(): Activity {
                return activity
            }

            override fun openFileFinish() {
                mDocContainer?.postDelayed({
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

}