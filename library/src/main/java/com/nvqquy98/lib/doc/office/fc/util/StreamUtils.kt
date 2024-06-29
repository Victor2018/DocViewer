package com.nvqquy98.lib.doc.office.fc.util

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
        val `in`: InputStream?
        if (filePath.startsWith("/")) {
            `in` = FileInputStream(filePath)
            return `in`
        }
        // else if (filePath.startsWith("file://")){
        //     `in` = FileInputStream(filePath.substring(7))
        //     return `in`
        // }
        val uri = Uri.parse(filePath)
        Log.d("StreamUtils", "uri = $uri")

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
