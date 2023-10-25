package com.cherry.lib.doc.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.cherry.lib.doc.bean.FileType
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
    fun downloadFile(context: Context, assetName: String, filePath: String, fileName: String?){

        val dirPath = "${Environment.getExternalStorageDirectory()}/${filePath}"
        val outFile = File(dirPath)
        //Create New File if not present
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
}