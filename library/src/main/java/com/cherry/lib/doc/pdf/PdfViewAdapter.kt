package com.cherry.lib.doc.pdf

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.cherry.lib.doc.R
import com.cherry.lib.doc.util.ViewUtils.hide
import com.cherry.lib.doc.util.ViewUtils.show
import kotlinx.android.synthetic.main.list_item_pdf_page.view.*
import kotlinx.android.synthetic.main.pdf_view_page_loading_layout.view.*

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
    private val renderer: PdfRendererCore,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean
) :
    RecyclerView.Adapter<PdfViewAdapter.PdfPageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        return PdfPageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_pdf_page,parent,
                false)
        )
    }

    override fun getItemCount(): Int {
        return renderer.getPageCount()
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PdfPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
                handleLoadingForPage(position)

            itemView.pageView.setImageBitmap(null)
            itemView.pageView.clearAnimation()
            renderer.renderPage(position) { bitmap: Bitmap?, pageNo: Int ->
                if (pageNo == position) {
                    bitmap?.let {
                        itemView.container_view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            height =
                                (itemView.container_view.width.toFloat() / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
                            this.topMargin = pageSpacing.top
                            this.leftMargin = pageSpacing.left
                            this.rightMargin = pageSpacing.right
                            this.bottomMargin = pageSpacing.bottom
                        }
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

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                itemView.pdf_view_page_loading_progress.hide()
                return
            }

            if (renderer.pageExistInCache(position)) {
                itemView.pdf_view_page_loading_progress.hide()
            } else {
                itemView.pdf_view_page_loading_progress.show()
            }
        }
    }
}