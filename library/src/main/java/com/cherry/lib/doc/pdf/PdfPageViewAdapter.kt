package com.cherry.lib.doc.pdf

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.cherry.lib.doc.R
import com.cherry.lib.doc.util.ViewUtils.hide
import com.cherry.lib.doc.util.ViewUtils.show

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PdfPageViewAdapter
 * Author: Victor
 * Date: 2023/09/28 11:17
 * Description: 
 * -----------------------------------------------------------------
 */

internal class PdfPageViewAdapter(
    private val renderer: PdfRendererCore?,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean
) :
    RecyclerView.Adapter<PdfPageViewAdapter.PdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        return PdfPageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.page_item_pdf,parent,
                false)
        )
    }

    override fun getItemCount(): Int {
        return renderer?.getPageCount() ?: 0
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bindView()
    }

    inner class PdfPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnAttachStateChangeListener {
        private lateinit var pdf_view_page_loading_progress: ProgressBar
        private lateinit var pageView: ImageView

        fun bindView() {
        }

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                pdf_view_page_loading_progress.hide()
                return
            }

            if (renderer?.pageExistInCache(position) == true) {
                pdf_view_page_loading_progress.hide()
            } else {
                pdf_view_page_loading_progress.show()
            }
        }

        init {
            pdf_view_page_loading_progress = itemView.findViewById(R.id.pdf_view_page_loading_progress)
            pageView = itemView.findViewById(R.id.pageView)

            itemView.addOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(p0: View) {
            handleLoadingForPage(adapterPosition)
            renderer?.renderPage(adapterPosition) { bitmap: Bitmap?, pageNo: Int ->
                if (pageNo == adapterPosition) {
                    bitmap?.let {
//                        itemView.container_view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                            height =
//                                (itemView.container_view.width.toFloat() / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
//                            this.topMargin = pageSpacing.top
//                            this.leftMargin = pageSpacing.left
//                            this.rightMargin = pageSpacing.right
//                            this.bottomMargin = pageSpacing.bottom
//                        }
                        pageView.setImageBitmap(bitmap)
                        pageView.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 200
                        }
                        pdf_view_page_loading_progress.hide()
                    }
                }
            }
        }

        override fun onViewDetachedFromWindow(p0: View) {
            pageView.setImageBitmap(null)
            pageView.clearAnimation()
        }
    }
}