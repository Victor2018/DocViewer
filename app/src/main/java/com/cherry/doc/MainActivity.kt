package com.cherry.doc

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cherry.doc.util.DocUtil
import com.cherry.lib.doc.DocViewerActivity
import com.cherry.lib.doc.bean.DocSourceType
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.util.FileUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),OnClickListener,OnItemClickListener {

    private val REQUEST_CODE_LOAD = 367
//    var url = "http://cdn07.foxitsoftware.cn/pub/foxit/manual/phantom/en_us/API%20Reference%20for%20Application%20Communication.pdf"
    var url = "https://hok-one.oss-accelerate.aliyuncs.com/hok_admin/data/decfdaa013b94fb193c215fc0ceb9c15.pdf"
//    var url = "https://xdts.xdocin.com/demo/resume3.docx"
//    var url = "http://172.16.28.95:8080/data/test2.ppt"
//    var url = "http://172.16.28.95:8080/data/testdocx.ll"

    var mDocAdapter: DocAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_assets -> {
                openDoc("test.docx",DocSourceType.ASSETS)
                return true
            }
            R.id.action_online -> {
                openDoc(url,DocSourceType.URL,null)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun initView() {
        setSupportActionBar(toolbar)

        mDocAdapter = DocAdapter(this,this)
        mRvDoc.adapter = mDocAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val isExternalStorageManager = Environment.isExternalStorageManager()
            if (!isExternalStorageManager) {
                get11Permission()
            }
        }
    }

    fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            var datas = DocUtil.getDocFile(this@MainActivity)
            CoroutineScope(Dispatchers.Main).launch {
                mDocAdapter?.showDatas(datas)
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
//            R.id.mBtnOnline -> {
//                if (checkSupport(url)) {
//                    openDoc(url,DocSourceType.URL)
//                }
//            }
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

    fun openDoc(path: String,docSourceType: Int,type: Int? = null) {
        DocViewerActivity.launchDocViewer(this,docSourceType,path,type)
    }

    override fun onItemClick(p0: AdapterView<*>?, v: View?, position: Int, id: Long) {
        when (v?.id) {
            R.id.mCvDocCell -> {
                val groupInfo = mDocAdapter?.datas?.get(id.toInt())
                val docInfo = groupInfo?.docList?.get(position)
                var path = docInfo?.path ?: ""
                if (checkSupport(path)) {
                    openDoc(path,DocSourceType.PATH)
                }
            }
        }

    }

}