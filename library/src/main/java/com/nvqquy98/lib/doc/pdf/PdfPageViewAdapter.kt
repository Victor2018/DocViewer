package com.nvqquy98.lib.doc.pdf

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.nvqquy98.lib.doc.R
import com.nvqquy98.lib.doc.util.ViewUtils.hide
import com.nvqquy98.lib.doc.util.ViewUtils.show
import kotlinx.android.synthetic.main.page_item_pdf.view.*
import kotlinx.android.synthetic.main.pdf_view_page_loading_layout.view.*

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

        fun bindView() {
        }

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                itemView.pdf_view_page_loading_progress.hide()
                return
            }

            if (renderer?.pageExistInCache(position) == true) {
                itemView.pdf_view_page_loading_progress.hide()
            } else {
                itemView.pdf_view_page_loading_progress.show()
            }
        }

        init {
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
                        itemView.pageView.setImageBitmap(bitmap)
                        itemView.pageView.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 200
                        }
                        itemView.pdf_view_page_loading_progress.hide()
                    }
                }
            }
        }

        override fun onViewDetachedFromWindow(p0: View) {
            itemView.pageView.setImageBitmap(null)
            itemView.pageView.clearAnimation()
        }
    }
}
