/*
 * 文件名称:          AbstractControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:59:01
 */
package com.nvqquy98.lib.doc.office.system

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.nvqquy98.lib.doc.office.common.ICustomDialog
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture
import com.nvqquy98.lib.doc.office.common.ISlideShow

/**
 * control抽象类
 *
 *
 *
 *
 * Read版本:        Read V1.0
 *
 *
 * 作者:            ljj8494
 *
 *
 * 日期:            2012-3-27
 *
 *
 * 负责人:          ljj8494
 *
 *
 * 负责小组:
 *
 *
 *
 *
 */
abstract class AbstractControl : IControl {
    override fun layoutView(x: Int, y: Int, w: Int, h: Int) {}
    override fun actionEvent(actionID: Int, obj: Any?) {}
    override fun getActionValue(actionID: Int, obj: Any?): Any? {
        return null
    }

    override fun getCurrentViewIndex(): Int {
        return -1
    }

    override fun getView(): View? {
        return null
    }

    override fun getDialog(activity: Activity, id: Int): Dialog? {
        return null
    }

    override fun getMainFrame(): IMainFrame? {
        return null
    }

    override fun getActivity(): Activity? {
        return null
    }

    override fun getFind(): IFind? {
        return null
    }

    override fun dispose() {}
    override fun isAutoTest(): Boolean {
        return false
    }

    override fun getOfficeToPicture(): IOfficeToPicture? {
        return null
    }

    /**
     *
     */
    override fun getCustomDialog(): ICustomDialog? {
        return null
    }

    /**
     *
     */
    override fun isSlideShow(): Boolean {
        return false
    }

    /**
     *
     * @return
     */
    override fun getSlideShow(): ISlideShow? {
        return null
    }

    override fun openFile(filePath: String): Boolean {
        return false
    }

    override fun getReader(): IReader? {
        return null
    }

    override fun getApplicationType(): Int {
        return -1
    }
}
