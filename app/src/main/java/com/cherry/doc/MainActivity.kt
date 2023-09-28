package com.cherry.doc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.cherry.doc.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(),OnClickListener {
    private val REQUEST_CODE_LOAD = 367
    var url = "https://oss.hokkj.cn/hok_admin/data/7d8dd243b8074f439157958623247f16.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    fun initView() {
        mBtnSelectFile.setOnClickListener(this)
        mBtnPdf.setOnClickListener(this)
    }

    fun selectFileAction() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent,REQUEST_CODE_LOAD)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnSelectFile -> {
                selectFileAction()
            }
            R.id.mBtnPdf -> {
                PdfViewerActivity.launchPdfFromUrl(this,url)
//                PdfViewerActivity.launchPdfFromAssets(this,"test.pdf")

                if (!PermissionHelper.hasPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    !PermissionHelper.hasPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionHelper.requestPermission(this, arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE),6)
                    return
                }
//                PdfViewerActivity.launchPdfFromPath(this,"/storage/emulated/0/test.pdf")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOAD && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                PdfViewerActivity.launchPdfFromUri(this,uri.toString())
            }
        }
    }
}