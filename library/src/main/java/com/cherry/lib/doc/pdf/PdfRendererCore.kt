package com.cherry.lib.doc.pdf

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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

    private fun getBitmapFromCache(pageNo: Int): Bitmap? {
        val loadPath = File(File(context.cacheDir, cachePath), pageNo.toString())
        if (!loadPath.exists())
            return null

        return try {
            BitmapFactory.decodeFile(loadPath.absolutePath)
        } catch (e: Exception) {
            null
        }
    }
    fun pageExistInCache(pageNo: Int): Boolean {
        val loadPath = File(File(context.cacheDir, cachePath), pageNo.toString())

        return loadPath.exists()
    }

    @Throws(IOException::class)
    private fun writeBitmapToCache(pageNo: Int, bitmap: Bitmap) {
        val savePath = File(File(context.cacheDir, cachePath), pageNo.toString())
        savePath.createNewFile()
        val fos = FileOutputStream(savePath)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
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

    fun getPageCount(): Int = pdfRenderer?.pageCount ?: 0

    fun renderPage(pageNo: Int, onBitmapReady: ((bitmap: Bitmap?, pageNo: Int) -> Unit)? = null) {
        if (pageNo >= getPageCount())
            return

        CoroutineScope(Dispatchers.IO).launch {
            synchronized(this@PdfRendererCore) {
                buildBitmap(pageNo) { bitmap ->
                    CoroutineScope(Dispatchers.Main).launch { onBitmapReady?.invoke(bitmap, pageNo) }
                }
                onBitmapReady?.let {
                    //prefetchNext(pageNo + 1)
                }
            }
        }
    }

    private fun prefetchNext(pageNo: Int) {
        val countForPrefetch = min(getPageCount(), pageNo + PREFETCH_COUNT)
        for (pageToPrefetch in pageNo until countForPrefetch) {
            renderPage(pageToPrefetch)
        }
    }

    private fun buildBitmap(pageNo: Int, onBitmap: (Bitmap?) -> Unit) {
        var bitmap = getBitmapFromCache(pageNo)
        bitmap?.let {
            onBitmap(it)
            return@buildBitmap
        }

        try {
            val pdfPage = pdfRenderer!!.openPage(pageNo)
            bitmap = Bitmap.createBitmap(
                pdfPage.width * pdfQuality.ratio,
                pdfPage.height * pdfQuality.ratio,
                Bitmap.Config.ARGB_8888
            )
            bitmap ?: return
            pdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfPage.close()
            writeBitmapToCache(pageNo, bitmap)

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