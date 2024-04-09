package com.cherry.lib.doc.office.fc.util

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.UriUtils
import java.io.FileInputStream
import java.io.InputStream

/**
 * @Author Jasper Jiao
 * @CreateTime 2024-04-08 18:18
 * @Des
 */
object StreamUtils {

    @Throws(Exception::class)
    @JvmStatic
    fun getInputStream(resolver: ContentResolver, filePath: String): InputStream? {
        Log.d("StreamUtils", "filePath = $filePath")
        val uri = Uri.parse(filePath)
        Log.d("StreamUtils", "uri = $uri")
        val `in`: InputStream?
        val file = UriUtils.uri2FileNoCacheCopy(Uri.parse(filePath))
        `in` = if (file != null) {
            Log.d("StreamUtils", "file.getAbsolutePath() = " + file.absolutePath)
            FileInputStream(file)
        } else if (uri.scheme != null) {
            // Log.d("StreamUtils", "uri.scheme = " + uri.scheme)
            resolver.openInputStream(uri)
        } else {
            // Log.d("StreamUtils", "filePath2 = $filePath")
            FileInputStream(filePath)
        }
        return `in`
    }
}