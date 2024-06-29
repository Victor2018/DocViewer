package com.nvqquy98.lib.doc.pdf

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PdfRendererCore
 * Author: Victor
 * Date: 2023/09/28 11:16
 * Description: 
 * -----------------------------------------------------------------
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal class PdfRendererCore(
    private val context: Context,
    pdfFile: File,
    private val pdfQuality: PdfQuality
) {
    companion object {
        var  pdfRenderer: PdfRenderer? = null
        private const val PREFETCH_COUNT = 3
    }

    private val cachePath = "___pdf___cache___"


    init {
        initCache()
        openPdfFile(pdfFile)
    }

    private fun initCache() {
        val cache = File(context.cacheDir, cachePath)
        if (cache.exists())
            cache.deleteRecursively()
        cache.mkdirs()
    }

    private fun getBitmapFromCache(pageNo: Int,quality: PdfQuality? = pdfQuality): Bitmap? {
        val loadPath = File(File(context.cacheDir, cachePath), "$quality-$pageNo")
        if (!loadPath.exists())
            return null

        return try {
            BitmapFactory.decodeFile(loadPath.absolutePath)
        } catch (e: Exception) {
            null
        }
    }
    fun pageExistInCache(pageNo: Int,quality: PdfQuality? = pdfQuality): Boolean {
        val loadPath = File(File(context.cacheDir, cachePath), "$quality-$pageNo")
        return loadPath.exists()
    }

    private fun writeBitmapToCache(pageNo: Int, quality: PdfQuality? = pdfQuality, bitmap: Bitmap) {
        try {
            val savePath = File(File(context.cacheDir, cachePath), "$quality-$pageNo")
            savePath.createNewFile()
            val fos = FileOutputStream(savePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openPdfFile(pdfFile: File) {
        try {
            val fileDescriptor =
                ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPageCount(): Int {
        var pageCount = 0
        try {
            pageCount = pdfRenderer?.pageCount ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pageCount
    }

    fun renderPage(pageNo: Int, quality: PdfQuality? = pdfQuality, onBitmapReady: ((bitmap: Bitmap?, pageNo: Int) -> Unit)? = null) {
        Timber.e("renderPage quality= $quality")
        if (pageNo >= getPageCount())
            return

        try {
            CoroutineScope(Dispatchers.IO).launch {
                synchronized(this@PdfRendererCore) {
                    buildBitmap(pageNo,quality) { bitmap ->
                        CoroutineScope(Dispatchers.Main).launch { onBitmapReady?.invoke(bitmap, pageNo) }
                    }
                    onBitmapReady?.let {
                        prefetchNext(pageNo + 1,quality)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prefetchNext(pageNo: Int,quality: PdfQuality? = pdfQuality) {
        val countForPrefetch = min(getPageCount(), pageNo + PREFETCH_COUNT)
        for (pageToPrefetch in pageNo until countForPrefetch) {
            renderPage(pageToPrefetch,quality)
        }
    }

    private fun buildBitmap(pageNo: Int, quality: PdfQuality? = pdfQuality, onBitmap: (Bitmap?) -> Unit) {
        var bitmap = getBitmapFromCache(pageNo,quality)
        bitmap?.let {
            onBitmap(it)
            return@buildBitmap
        }

        try {
            val ratio = quality?.ratio ?: 1
            val pdfPage = pdfRenderer!!.openPage(pageNo)
            bitmap = Bitmap.createBitmap(
                pdfPage.width * ratio,
                pdfPage.height * ratio,
                Bitmap.Config.ARGB_8888
            )
            bitmap ?: return
            pdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfPage.close()
            writeBitmapToCache(pageNo,quality, bitmap)

            onBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closePdfRender() {
        try {
            pdfRenderer?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
