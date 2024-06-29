package com.nvqquy98.lib.doc.office.fc.util

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.UriUtils
import timber.log.Timber
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
        Timber.d("StreamUtils", "filePath = $filePath")
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
        Timber.d("StreamUtils", "uri = $uri")

        val file = UriUtils.uri2FileNoCacheCopy(Uri.parse(filePath))
        `in` = if (file != null) {
            Timber.d("StreamUtils", "file.getAbsolutePath() = " + file.absolutePath)
            FileInputStream(file)
        } else if (uri.scheme != null) {
            // Timber.d("StreamUtils", "uri.scheme = " + uri.scheme)
            resolver.openInputStream(uri)
        } else {
            // Timber.d("StreamUtils", "filePath2 = $filePath")
            FileInputStream(filePath)
        }
        return `in`
    }
}
