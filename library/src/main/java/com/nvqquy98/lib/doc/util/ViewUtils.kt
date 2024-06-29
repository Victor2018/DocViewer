package com.nvqquy98.lib.doc.util

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ViewUtils
 * Author: Victor
 * Date: 2023/09/28 10:53
 * Description: 
 * -----------------------------------------------------------------
 */

object ViewUtils {
    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun getViewByLayout (context: Context?, layoutId: Int): View {
        var inflater = LayoutInflater.from(context)
        return inflater.inflate(layoutId, null)
    }

    fun getViewByLayout (inflater: LayoutInflater, layoutId: Int): View {
        return inflater.inflate(layoutId, null)
    }

    fun View.bitmap(): Bitmap {
        //不加下面两句，会报错：width and height must be > 0
        measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        layout(0, 0, measuredWidth, measuredHeight)

        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun generateViewCacheBitmap(view: View): Bitmap? {
        view.destroyDrawingCache()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthMeasureSpec, heightMeasureSpec)
        val width = view.measuredWidth
        val height = view.measuredHeight
        view.layout(0, 0, width, height)
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return Bitmap.createBitmap(view.drawingCache)
    }

    fun findBrotherView(view: View, id: Int, level: Int): View? {
        var count = 0
        var temp = view
        while (count < level) {
            val target = temp.findViewById<View>(id)
            if (target != null) {
                return target
            }
            count += 1
            temp = if (temp.parent is View) {
                temp.parent as View
            } else {
                break
            }
        }
        return null
    }

    fun View.addRipple() = with(TypedValue()) {
        context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
        setBackgroundResource(resourceId)
    }

    fun View.removeRipple() {
        setBackgroundResource(0)
    }

    fun View.addCircleRipple() = with(TypedValue()) {
        context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, this, true)
        setBackgroundResource(resourceId)
    }
}
