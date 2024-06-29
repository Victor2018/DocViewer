package com.cherry.doc.util

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.cherry.doc.data.DocGroupInfo
import com.cherry.doc.data.DocInfo
import com.nvqquy98.lib.doc.bean.FileType
import com.nvqquy98.lib.doc.util.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DocUtil
 * Author: Victor
 * Date: 2023/10/26 10:16
 * Description: 
 * -----------------------------------------------------------------
 */

object DocUtil {
    private val TAG = "DocUtil"
    fun getDocFile(context: Context): ArrayList<DocGroupInfo> {
        val pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
        val docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx")
        val xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls")
        val xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx")
        val ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt")
        val pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx")
        val txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
        val png = MimeTypeMap.getSingleton().getMimeTypeFromExtension("png")
        val jpg = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")
        val jpeg = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
        //Table
        val table = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        //Column
        val column = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.ALBUM
        )
        //Where
        val selection = (MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?")
        //args
        val selectionArgs = arrayOf(pdf, doc, docx, xls, xlsx, ppt, pptx, txt,png,jpg,jpeg)
        val fileCursor = context.contentResolver.query(table, column, selection, selectionArgs, null)

        val displayNameKey = fileCursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        val dataKey = fileCursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val sizeKey = fileCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val typeKey = fileCursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
        val lastModifiedKey = fileCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
        val albumKey = fileCursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)

        val docGroupList = ArrayList<DocGroupInfo>()
        val docList = ArrayList<DocInfo>()
        val excelList = ArrayList<DocInfo>()
        val pptList = ArrayList<DocInfo>()
        val pdfList = ArrayList<DocInfo>()
        val txtList = ArrayList<DocInfo>()
        val imageList = ArrayList<DocInfo>()

        while (fileCursor.moveToNext()) {
            val fileName = fileCursor.getString(displayNameKey)
            val path = fileCursor.getString(dataKey)
            val mimeType = fileCursor.getString(typeKey)
            val album = fileCursor.getString(albumKey)
            val lastModified = fileCursor.getLong(lastModifiedKey)
            val fileSize = fileCursor.getLong(sizeKey)
            Log.v(TAG,"fileName = $fileName")
            Log.v(TAG,"path = $path")
            Log.v(TAG,"mimeType = $mimeType")
            Log.v(TAG,"fileSize = ${getFormatSize(fileSize.toDouble())}")
            Log.v(TAG,"lastModified = ${stampToDate(lastModified * 1000)}")

            var item = DocInfo()
            item.album = album
            item.fileName = fileName
            item.path = path
            item.mimeType = mimeType
            item.lastModified = stampToDate(lastModified * 1000)
            item.fileSize = getFormatSize(fileSize.toDouble())

            var fileType = FileUtils.getFileTypeForUrl(path)
            if (fileType == FileType.DOC || fileType == FileType.DOCX) {
                docList.add(item)
            }
            if (fileType == FileType.XLS || fileType == FileType.XLSX) {
                excelList.add(item)
            }
            if (fileType == FileType.PPT || fileType == FileType.PPTX) {
                pptList.add(item)
            }
            if (fileType == FileType.PDF) {
                pdfList.add(item)
            }
            if (fileType == FileType.TXT) {
                txtList.add(item)
            }
            if (fileType == FileType.IMAGE) {
                imageList.add(item)
            }

        }


        docGroupList.add(DocGroupInfo("DOC & DOCX",docList))
        docGroupList.add(DocGroupInfo("XLS & XLSX",excelList))
        docGroupList.add(DocGroupInfo("PPT & PPTX",pptList))
        docGroupList.add(DocGroupInfo("PDF",pdfList))
        docGroupList.add(DocGroupInfo("TXT",txtList))
        docGroupList.add(DocGroupInfo("IMAGE",imageList))

        return docGroupList
    }

    /*
     * 将时间戳转换为时间
     */
    fun stampToDate(s: Long): String? {
        var res: String? = null
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val date = Date(s)
            res = simpleDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    fun getFormatSize(size: Double): String? {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "${size}B"
//            return "0KB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 =
                BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 =
                BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 =
                BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB")
    }
}
