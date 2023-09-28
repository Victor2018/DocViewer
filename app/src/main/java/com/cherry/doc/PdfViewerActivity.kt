package com.cherry.doc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cherry.doc.util.Constant
import com.cherry.lib.doc.DocViewer
import com.cherry.lib.doc.pdf.PdfQuality
import com.cherry.lib.doc.pdf.PdfRendererView
import com.cherry.lib.doc.bean.DocSourceType
import kotlinx.android.synthetic.main.activity_pdf_viewer.*

class PdfViewerActivity : AppCompatActivity(), PdfRendererView.StatusCallBack {

    companion object {
        fun launchPdfFromUrl (activity: AppCompatActivity, url: String?) {
            var intent = Intent(activity, PdfViewerActivity::class.java)
            intent.putExtra(Constant.INTENT_SOURCE_KEY, DocSourceType.URL)
            intent.putExtra(Constant.INTENT_DATA_KEY,url)
            activity.startActivity(intent)
        }

        fun launchPdfFromUri (activity: AppCompatActivity, uri: String?) {
            var intent = Intent(activity, PdfViewerActivity::class.java)
            intent.putExtra(Constant.INTENT_SOURCE_KEY, DocSourceType.URI)
            intent.putExtra(Constant.INTENT_DATA_KEY,uri)
            activity.startActivity(intent)
        }

        fun launchPdfFromPath (activity: AppCompatActivity, path: String?) {
            var intent = Intent(activity, PdfViewerActivity::class.java)
            intent.putExtra(Constant.INTENT_SOURCE_KEY, DocSourceType.PATH)
            intent.putExtra(Constant.INTENT_DATA_KEY,path)
            activity.startActivity(intent)
        }

        fun launchPdfFromAssets (activity: AppCompatActivity, assestName: String?) {
            var intent = Intent(activity, PdfViewerActivity::class.java)
            intent.putExtra(Constant.INTENT_SOURCE_KEY, DocSourceType.ASSETS)
            intent.putExtra(Constant.INTENT_DATA_KEY,assestName)
            activity.startActivity(intent)
        }

    }

    var docSourceType = 0
    var pdfUrl: String? = null//文件地址

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        initView()
        initData(intent)
    }

    fun initView() {
    }

    fun initData(intent: Intent?) {
        pdfUrl = intent?.getStringExtra(Constant.INTENT_DATA_KEY)
        docSourceType = intent?.getIntExtra(Constant.INTENT_SOURCE_KEY,0) ?: 0

        mTvPdfUrl.text = pdfUrl

        DocViewer.showPdf(docSourceType,mPdfView,pdfUrl)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initData(intent)
    }
}