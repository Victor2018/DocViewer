package com.nvqquy98.lib.doc.util

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: Constant
 * Author: Victor
 * Date: 2023/09/28 10:36
 * Description: 
 * -----------------------------------------------------------------
 */

object Constant {
    const val INTENT_DATA_KEY = "INTENT_DATA_KEY"
    const val INTENT_SOURCE_KEY = "INTENT_SOURCE_KEY"
    const val INTENT_TYPE_KEY = "INTENT_TYPE_KEY"
    const val INTENT_ENGINE_KEY = "INTENT_ENGINE_KEY"
    const val INTENT_TITLE = "intent_title"
    const val INTENT_POSITION_KEY = "INTENT_POSITION_KEY"

    /**
     * Office 平台在线预览限制：
     * Word 和 PowerPoint 文档必须小于 10 M，Excel 必须小于 5M;
     * 文档支持的格式：
     * Word: docx、dotx
     * Excel：xlsx、xlsb、xls、xlsm
     * PowerPoint: pptx、 ppsx、 ppt、 pps、 potx、 ppsm
     */
    const val MICROSOFT_URL = "https://view.officeapps.live.com/op/view.aspx?src="

    /**
     * XDOC文档预览服务 支持pdf在线浏览
     */
    const val XDOC_VIEW_URL = "http://www.xdocin.com/xdoc?_func=to&_format=html&_cache=true&_xdoc="

    /**
     * google文档预览服务，科学上网
     */
    const val GOOGLE_URL = "https://drive.google.com/viewer/viewer?hl=en&embedded=true&url="
}
