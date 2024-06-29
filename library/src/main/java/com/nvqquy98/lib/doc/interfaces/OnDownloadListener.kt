package com.nvqquy98.lib.doc.interfaces

import android.content.Context
import kotlinx.coroutines.CoroutineScope

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnDownloadListener
 * Author: Victor
 * Date: 2023/10/31 10:55
 * Description: 
 * -----------------------------------------------------------------
 */

interface OnDownloadListener {
    fun getDownloadContext(): Context
    fun onDownloadStart() {}
    fun onDownloadProgress(currentBytes: Long, totalBytes: Long) {}
    fun onDownloadSuccess(absolutePath: String) {}
    fun onError(error: Throwable) {}
    fun getCoroutineScope(): CoroutineScope
}
