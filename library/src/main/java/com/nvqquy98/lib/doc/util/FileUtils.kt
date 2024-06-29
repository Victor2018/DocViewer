package com.nvqquy98.lib.doc.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.blankj.utilcode.util.AppUtils
import com.nvqquy98.lib.doc.bean.FileType
import java.io.*
import java.util.Locale

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: FileUtils
 * Author: Victor
 * Date: 2023/09/28 10:52
 * Description: 
 * -----------------------------------------------------------------
 */

object FileUtils {
    const val txtRe = "txt$"
    const val pdfRe = "pdf$"
    const val imageRe = "(?:jpg|gif|png|jpeg|webp)$"
    const val docRe = "doc$"
    const val docxRe = "docx$"
    const val xlsRe = "xls$"
    const val xlsxRe = "xlsx$"
    const val pptRe = "ppt$"
    const val pptxRe = "pptx$"
    val internalCacheDir: File
        get() = File(internalCacheDirPath).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    val internalFilesDir: File
        get() = File(internalFilesDirPath).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    private val internalCacheDirPath = "/data/data/" + AppUtils.getAppPackageName() + "/cache"
    private val internalFilesDirPath = "/data/data/" + AppUtils.getAppPackageName() + "/files"

    @Throws(IOException::class)
    fun fileFromUri(context: Context, fileUri: String): File {
        var uri = Uri.parse(fileUri)
        val input = context.contentResolver.openInputStream(uri)
        val outFile = File(context.cacheDir, "${uri.hashCode()}")
        copy(input, outFile)
        return outFile
    }

    @Throws(IOException::class)
    fun fileFromAsset(context: Context, assetName: String): File {
        val outFile = File(context.cacheDir, "$assetName")
        if (assetName.contains("/")) {
            outFile.parentFile.mkdirs()
        }
        copy(context.assets.open(assetName), outFile)
        return outFile
    }

    @Throws(IOException::class)
    fun copy(inputStream: InputStream?, output: File) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(output)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } finally {
            try {
                inputStream?.close()
            } finally {
                outputStream?.close()
            }
        }
    }

    @Throws(IOException::class)
    fun downloadFile(context: Context, assetName: String, filePath: String, fileName: String?) {
        val dirPath = "$internalCacheDirPath/${filePath}"
        val outFile = File(dirPath)
        // Create New File if not present
        if (!outFile.exists()) {
            outFile.mkdirs()
        }
        val outFile1 = File(dirPath, "/$fileName.pdf")
        copy(context.assets.open(assetName), outFile1)
    }

    fun getFileTypeForUrl(url: String?): Int {
        val str = url?.lowercase(Locale.getDefault()) ?: ""
        return when {
            txtRe.toRegex().containsMatchIn(str) -> FileType.TXT
            pdfRe.toRegex().containsMatchIn(str) -> FileType.PDF
            imageRe.toRegex().containsMatchIn(str) -> FileType.IMAGE
            docRe.toRegex().containsMatchIn(str) -> FileType.DOC
            docxRe.toRegex().containsMatchIn(str) -> FileType.DOCX
            xlsRe.toRegex().containsMatchIn(str) -> FileType.XLS
            xlsxRe.toRegex().containsMatchIn(str) -> FileType.XLSX
            pptRe.toRegex().containsMatchIn(str) -> FileType.PPT
            pptxRe.toRegex().containsMatchIn(str) -> FileType.PPTX
            else -> FileType.NOT_SUPPORT
        }
    }

    fun getFileFormatForUrl(url: String?): String {
        val str = url?.lowercase(Locale.getDefault()) ?: ""
        return when {
            txtRe.toRegex().containsMatchIn(str) -> "txt"
            pdfRe.toRegex().containsMatchIn(str) -> "pdf"
            imageRe.toRegex().containsMatchIn(str) -> "png"
            docRe.toRegex().containsMatchIn(str) -> "doc"
            docxRe.toRegex().containsMatchIn(str) -> "docx"
            xlsRe.toRegex().containsMatchIn(str) -> "xls"
            xlsxRe.toRegex().containsMatchIn(str) -> "xlsx"
            pptRe.toRegex().containsMatchIn(str) -> "ppt"
            pptxRe.toRegex().containsMatchIn(str) -> "pptx"
            else -> "unknown"
        }
    }

    val mimeExtMap = mapOf(
        "video/3gpp" to ".3gp",
        "application/vnd.android.package-archive" to ".apk",
        "video/x-ms-asf" to ".asf",
        "video/x-msvideo" to ".avi",
        // "application/octet-stream" to ".bin",
        "image/bmp" to ".bmp",
        // "application/octet-stream" to ".class",
        // "text/plain" to ".conf",
        // "text/plain" to ".cpp",
        "application/msword" to ".doc",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to ".docx",
        "application/vnd.ms-excel" to ".xls",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to ".xlsx",
        "application/octet-stream" to ".exe",
        "image/gif" to ".gif",
        "application/x-gtar" to ".gtar",
        // "text/html" to ".htm",
        "text/html" to ".html",
        "application/java-archive" to ".jar",
        // "text/plain" to ".java",
        // "image/jpeg" to ".jpeg",
        "image/jpeg" to ".jpg",
        "application/x-javascript" to ".js",
        // "text/plain" to ".log",
        "audio/x-mpegurl" to ".m3u",
        "audio/mp4a-latm" to ".m4a",
        // "audio/mp4a-latm" to ".m4b",
        // "audio/mp4a-latm" to ".m4p",
        "video/vnd.mpegurl" to ".m4u",
        "video/x-m4v" to ".m4v",
        "video/quicktime" to ".mov",
        // "audio/x-mpeg" to ".mp2",
        "audio/x-mpeg" to ".mp3",
        "video/mp4" to ".mp4",
        "application/vnd.mpohun.certificate" to ".mpc",
        // "video/mpeg" to ".mpe",
        "video/mpeg" to ".mpeg",
        // "video/mpeg" to ".mpg",
        // "video/mp4" to ".mpg4",
        "audio/mpeg" to ".mpga",
        "application/vnd.ms-outlook" to ".msg",
        "audio/ogg" to ".ogg",
        "application/pdf" to ".pdf",
        "image/png" to ".png",
        // "application/vnd.ms-powerpoint" to ".pps",
        "application/vnd.ms-powerpoint" to ".ppt",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation" to ".pptx",
        // "text/plain" to ".prop",
        // "text/plain" to ".rc",
        "audio/x-pn-realaudio" to ".rmvb",
        "application/rtf" to ".rtf",
        // "text/plain" to ".sh",
        "application/x-tar" to ".tar",
        "application/x-compressed" to ".tgz",
        "text/plain" to ".txt",
        "audio/x-wav" to ".wav",
        "audio/x-ms-wma" to ".wma",
        "audio/x-ms-wmv" to ".wmv",
        "application/vnd.ms-works" to ".wps",
        // "text/plain" to ".xml",
        "application/x-compress" to ".z",
        "application/x-zip-compressed" to ".zip",
        // "text/plain" to ".c",
        "application/x-gzip" to ".gz",
        // "text/plain" to ".h",
        "*/*" to "",
    )

    fun getFileMimeType(context: Context, contentUri: Uri): String? {
        val contentResolver = context.contentResolver
        var mimeType = contentResolver.getType(contentUri)

        // 如果系统未能直接返回MIME类型，尝试通过文件扩展名推测
        if (mimeType == null) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(contentUri.toString())
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return mimeType
    }
}
