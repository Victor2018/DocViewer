package com.cherry.lib.doc.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.cherry.lib.doc.R
import com.cherry.lib.doc.interfaces.OnPdfItemClickListener
import com.cherry.lib.doc.util.ViewUtils.hide
import com.cherry.lib.doc.util.ViewUtils.show

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PdfViewAdapter
 * Author: Victor
 * Date: 2023/09/28 11:17
 * Description: 
 * -----------------------------------------------------------------
 */

internal class PdfViewAdapter(
    private val renderer: PdfRendererCore?,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean,
    private val listener: OnPdfItemClickListener?
) :
    RecyclerView.Adapter<PdfViewAdapter.PdfPageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        return PdfPageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_pdf,parent,
                false)
        )
    }

    override fun getItemCount(): Int {
        try {
            return renderer?.getPageCount() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bindView()
    }

    inner class PdfPageViewHolder : RecyclerView.ViewHolder,View.OnAttachStateChangeListener {
        private var container_view: FrameLayout? = null
        private var pdf_view_page_loading_progress: ProgressBar? = null
        private var pageView: ImageView? = null

        constructor(itemView: View): super(itemView) {
            container_view = itemView.findViewById(R.id.container_view)
            pdf_view_page_loading_progress = itemView.findViewById(R.id.pdf_view_page_loading_progress)
            pageView = itemView.findViewById(R.id.pageView)

            container_view?.setOnClickListener {
                listener?.OnPdfItemClick(adapterPosition)
            }

            itemView.addOnAttachStateChangeListener(this)
        }

        fun bindView() {

        }

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                pdf_view_page_loading_progress?.hide()
                return
            }

            if (renderer?.pageExistInCache(position) == true) {
                pdf_view_page_loading_progress?.hide()
            } else {
                pdf_view_page_loading_progress?.show()
            }
        }

        override fun onViewAttachedToWindow(p0: View) {
            handleLoadingForPage(adapterPosition)
            renderer?.renderPage(adapterPosition) { bitmap: Bitmap?, pageNo: Int ->
                if (pageNo == adapterPosition) {
                    bitmap?.let {
                        container_view?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            height =
                                (container_view?.width?.toFloat() ?: 0f / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
                            this.topMargin = pageSpacing.top
                            this.leftMargin = pageSpacing.left
                            this.rightMargin = pageSpacing.right
                            this.bottomMargin = pageSpacing.bottom
                        }
                        pageView?.setImageBitmap(bitmap)
                        pageView?.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 200
                        }
                        pdf_view_page_loading_progress?.hide()
                    }
                }
            }
        }

        override fun onViewDetachedFromWindow(p0: View) {
            pageView?.setImageBitmap(null)
            pageView?.clearAnimation()
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)

        //canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
}