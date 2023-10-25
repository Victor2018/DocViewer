package com.cherry.doc

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cherry.lib.doc.bean.DocSourceType
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.util.FileUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),OnClickListener {

    private val REQUEST_CODE_LOAD = 367
    var url = "https://oss.hokkj.cn/hok_admin/data/7d8dd243b8074f439157958623247f16.pdf"
//    var url = "http://172.16.28.95:8080/data/test2.ppt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    fun initView() {

        mBtnSelectFile.setOnClickListener(this)
        mBtnOnline.setOnClickListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val isExternalStorageManager = Environment.isExternalStorageManager()
            if (!isExternalStorageManager) {
                get11Permission()
            }
        }
    }

    fun get11Permission() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(java.lang.String.format("package:%s", packageName))
            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            startActivityForResult(intent, 100)
        }
    }


    fun selectFileAction() {

        val intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.type = "*/*";
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_LOAD);
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnSelectFile -> {
                selectFileAction()
            }
            R.id.mBtnOnline -> {
                if (checkSupport(url)) {
                    openDoc(url,DocSourceType.URL)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOAD -> {
                    val uri = data?.data
                    if (uri != null) {
                        if (checkSupport(uri.toString())) {
                            openDoc(uri.toString(),DocSourceType.URI)
                        }
                    }
                }
            }
        }
    }

    fun checkSupport(path: String): Boolean {
        var fileType = FileUtils.getFileTypeForUrl(path)
        Log.e(javaClass.simpleName,"fileType = $fileType")
        if (fileType == FileType.NOT_SUPPORT) {
            return false
        }
        return true
    }

    fun openDoc(path: String,docSourceType: Int) {
        var fileType = FileUtils.getFileTypeForUrl(path)
        Log.e(javaClass.simpleName,"fileType = $fileType")
        when (fileType) {
            FileType.NOT_SUPPORT -> {
                Toast.makeText(this,"不支持的文档格式",Toast.LENGTH_SHORT).show()
            }
            else -> {
                DocViewerActivity.launchDocViewer(this,docSourceType,path)
            }
        }
    }

}