package com.cherry.lib.doc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cherry.lib.doc.bean.DocEngine
import com.cherry.lib.doc.util.Constant
import kotlinx.android.synthetic.main.activity_doc_viewer.mDocView

open class DocViewerActivity : AppCompatActivity() {
    private val TAG = "DocViewerActivity"

    companion object {
        fun launchDocViewer(
            activity: AppCompatActivity, docSourceType: Int, path: String?,
            fileType: Int? = null, engine: Int? = null
        ) {
            var intent = Intent(activity, DocViewerActivity::class.java)
            intent.putExtra(Constant.INTENT_SOURCE_KEY, docSourceType)
            intent.putExtra(Constant.INTENT_DATA_KEY, path)
            intent.putExtra(Constant.INTENT_TYPE_KEY, fileType)
            intent.putExtra(Constant.INTENT_ENGINE_KEY, engine)
            activity.startActivity(intent)
        }
    }

    var docSourceType = 0
    var fileType = -1
    var engine: Int = DocEngine.INTERNAL.value
    var docUrl: String? = null// 文件地址

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_viewer)

        initView()
        initData(intent)
    }

    fun initView() {
    }

    fun initData(intent: Intent?) {
        docUrl = intent?.getStringExtra(Constant.INTENT_DATA_KEY)
        docSourceType = intent?.getIntExtra(Constant.INTENT_SOURCE_KEY, 0) ?: 0
        fileType = intent?.getIntExtra(Constant.INTENT_TYPE_KEY, -1) ?: -1
        engine = intent?.getIntExtra(Constant.INTENT_ENGINE_KEY, DocEngine.INTERNAL.value) ?: DocEngine.INTERNAL.value

        mDocView.openDoc(this,docUrl,docSourceType,fileType,false, DocEngine.values().first { it.value == engine })
        Log.e(TAG, "initData-docUrl = $docUrl")
        Log.e(TAG, "initData-docSourceType = $docSourceType")
        Log.e(TAG, "initData-fileType = $fileType")
        Log.e(TAG, "initData-engine = $engine")
    }

}