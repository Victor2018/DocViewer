package com.nvqquy98.lib.doc.office.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import com.nvqquy98.lib.doc.office.common.bookmark.BookmarkManage
import com.nvqquy98.lib.doc.office.common.borders.BordersManage
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListManage
import com.nvqquy98.lib.doc.office.common.hyperlink.HyperlinkManage
import com.nvqquy98.lib.doc.office.common.picture.PictureManage
import com.nvqquy98.lib.doc.office.pg.animate.AnimationManager
import com.nvqquy98.lib.doc.office.pg.model.PGBulletText
import com.nvqquy98.lib.doc.office.system.beans.CalloutView.CalloutManager
import com.nvqquy98.lib.doc.office.wp.control.WPShapeManage
import java.io.File
import kotlin.experimental.and

class SysKit(
    /**
     * @param control The control to set.
     */
    // 保存一个control实例，这个control实例就是AppActivity
    var control: IControl?
) {
    init {
        getErrorKit(this)
    }
    // 
    val sDPath: File?
        get() {
            // 
            if (File("/mnt/extern_sd").exists() || File("/mnt/usbhost1").exists()) {
                return File("/mnt")
            }
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == state) {
                Environment.getExternalStorageDirectory()
            } else null
        }

    /**
     * 获取存储卡的剩余容量，单位为字节
     * @param filePath
     * @return availableSpare
     */
    fun getAvailableStore(filePath: String?): Long {
        // 取得sdcard文件路径
        val statFs = StatFs(filePath)
        // 获取block的SIZE
        val blocSize = statFs.blockSize.toLong()
        // 可使用的Block的数量
        val availaBlock = statFs.availableBlocks.toLong()
        return availaBlock * blocSize
    }

    /**
     * 判断是否竖屏
     */
    fun isVertical(context: Context): Boolean {
        return context.resources.configuration.orientation ==
                Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * convert character encode
     *
     * @param str
     * @return
     */
    fun charsetEncode(str: String, encode: String?): String {
        var str = str
        if ("" == str) {
            return ""
        }
        val strBuff = StringBuffer("")
        try {
            val b = str.toByteArray(charset(encode!!))
            for (n in b.indices) {
                str = Integer.toHexString((b[n] and 0XFF.toByte()).toInt())
                if (str.length == 1) {
                    strBuff.append("0").append(str)
                } else {
                    strBuff.append(str)
                }
            }
            val chs = strBuff.toString().toCharArray()
            strBuff.delete(0, strBuff.length)
            var i = 0
            while (i < chs.size) {
                strBuff.append("%").append(chs[i]).append(chs[i + 1])
                i = i + 2
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strBuff.toString()
    }

    /**
     * Internet search
     *
     * @param word
     */
    fun internetSearch(str: String, activity: Activity) {
        var str = str
        str = charsetEncode(str, "utf-8")
        var url =
            "http://www.google.com.hk/#hl=en&newwindow=1&safe=strict&site=&q=a-a-a-a&oq=a-a-a-a&aq=f&aqi=&aql=&gs_sm=3" +
                    "&gs_upl=1075l1602l0l1935l3l3l0l0l0l0l0l0ll0l0&gs_l=hp.3...1075l1602l0l1935l3l3l0l0l0l0l0l0ll0l0&bav=on.2,or.r_gc.r_pw.,cf.osb" +
                    "&fp=207f1fbbc21b7536&biw=1280&bih=876"
        url = url.replace("a-a-a-a".toRegex(), str)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
    }

    /**
     *
     */
    val isDebug: Boolean
        get() = false

    /**
     *
     */
    val pictureManage: PictureManage
        get() {
            if (pmKit == null) {
                pmKit = PictureManage(control)
            }
            return pmKit as PictureManage
        }

    /**
     *
     */
    val hyperlinkManage: HyperlinkManage
        get() {
            if (hmKit == null) {
                hmKit = HyperlinkManage()
            }
            return hmKit as HyperlinkManage
        }

    /**
     *
     * @return
     */
    val listManage: ListManage
        get() {
            if (lmKit == null) {
                lmKit = ListManage()
            }
            return lmKit as ListManage
        }

    /**
     *
     */
    val pGBulletText: PGBulletText
        get() {
            if (pgLMKit == null) {
                pgLMKit = PGBulletText()
            }
            return pgLMKit as PGBulletText
        }

    /**
     *
     */
    val bordersManage: BordersManage
        get() {
            if (brKit == null) {
                brKit = BordersManage()
            }
            return brKit as BordersManage
        }

    /**
     *
     */
    val wPShapeManage: WPShapeManage
        get() {
            if (wpSMKit == null) {
                wpSMKit = WPShapeManage()
            }
            return wpSMKit as WPShapeManage
        }

    /**
     *
     * @return
     */
    val bookmarkManage: BookmarkManage
        get() {
            if (bmKit == null) {
                bmKit = BookmarkManage()
            }
            return bmKit as BookmarkManage
        }

    /**
     *
     * @return
     */
    val animationManager: AnimationManager
        get() {
            if (animationMgr == null) {
                animationMgr = AnimationManager(control)
            }
            return animationMgr as AnimationManager
        }

    /**
     *
     */
    val calloutManager: CalloutManager
        get() {
            if (calloutMgr == null) {
                calloutMgr = CalloutManager(control)
            }
            return calloutMgr as CalloutManager
        }

    /**
     *
     */
    fun dispose() {
        control = null
        if (errorKit != null) {
            errorKit!!.dispose()
            errorKit = null
        }
        if (pmKit != null) {
            pmKit!!.dispose()
            pmKit = null
        }
        if (hmKit != null) {
            hmKit!!.dispose()
            hmKit = null
        }
        if (lmKit != null) {
            lmKit!!.dispose()
            lmKit = null
        }
        if (pgLMKit != null) {
            pgLMKit!!.dispose()
            pgLMKit = null
        }
        if (brKit != null) {
            brKit!!.dispose()
            brKit = null
        }
        if (wpSMKit != null) {
            wpSMKit!!.dispose()
            wpSMKit = null
        }
        if (bmKit != null) {
            bmKit!!.dispose()
            bmKit = null
        }
        if (animationMgr != null) {
            animationMgr!!.dispose()
            animationMgr = null
        }
        if (calloutMgr != null) {
            calloutMgr!!.dispose()
            calloutMgr = null
        }
    }

    //
    var errorKit: ErrorUtil? = null

    //
    private var pmKit: PictureManage? = null

    //
    private var hmKit: HyperlinkManage? = null

    //
    private var lmKit: ListManage? = null

    //
    private var pgLMKit: PGBulletText? = null

    //
    private var brKit: BordersManage? = null

    //
    private var wpSMKit: WPShapeManage? = null

    //
    private var bmKit: BookmarkManage? = null

    /**
     * @return Returns the control.
     */
    //
    private var animationMgr: AnimationManager? = null

    //
    private var calloutMgr: CalloutManager? = null

    companion object {
        /**
         *
         */
        @JvmStatic
        val pageNubmerDrawable: Drawable?
            get() {
                if (pageNumberDrawable == null) {
                    pageNumberDrawable = ShapeDrawable(
                        RoundRectShape(
                            floatArrayOf(6f, 6f, 6f, 6f, 6f, 6f, 6f, 6f),
                            null,
                            null
                        )
                    )
                    pageNumberDrawable!!.paint.color = -0x770077bc
                }
                return pageNumberDrawable
            }

        /**
         * the whole interesting area is in the page area
         * @param pageWidth
         * @param pageHeight
         * @param x left position of interesting area
         * @param y top position of interesting area
         * @param width width of interesting area
         * @param height height of interesting area
         * @return
         */
        @JvmStatic
        fun isValidateRect(
            pageWidth: Int,
            pageHeight: Int,
            x: Int,
            y: Int,
            width: Int,
            height: Int
        ): Boolean {
            return x >= 0 && y >= 0 && x < pageWidth && y < pageHeight && width >= 0 && height >= 0 && x + width <= pageWidth && y + height <= pageHeight
        }

        //
        private var pageNumberDrawable: ShapeDrawable? = null

        /**
         *
         */
        fun getErrorKit(sysKit: SysKit): ErrorUtil {
            if (sysKit.errorKit == null) {
                sysKit.errorKit = ErrorUtil(sysKit)
            }
            return sysKit.errorKit!!
        }
    }
}
