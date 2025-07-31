package com.cherry.doc

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cherry.doc.data.DocInfo
import com.cherry.lib.doc.bean.FileType
import com.cherry.lib.doc.util.FileUtils
import java.io.File

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DocViewHolder
 * Author: Victor
 * Date: 2023/10/26 10:57
 * Description: 
 * -----------------------------------------------------------------
 */

class DocCellViewHolder : RecyclerView.ViewHolder,OnClickListener {
    var mOnItemClickListener: OnItemClickListener? = null
    var parentPosition: Int = 0
    constructor(itemView: View, groupPosition: Int) : super(itemView) {
        parentPosition = groupPosition
        itemView.setOnClickListener(this)
    }

    fun bindData(data: DocInfo?) {
        var typeIcon = data?.getTypeIcon() ?: -1
        if (typeIcon == -1) {
            var file = File(data?.path)
            if (file.exists()) {
                itemView.findViewById<ImageView>(R.id.mIvType).load(File(data?.path))
            } else {
                itemView.findViewById<ImageView>(R.id.mIvType).load(com.cherry.lib.doc.R.drawable.all_doc_ic)
            }
        } else {
            itemView.findViewById<ImageView>(R.id.mIvType).load(typeIcon)
        }
        itemView.findViewById<TextView>(R.id.mTvFileName).text = data?.fileName
        itemView.findViewById<TextView>(R.id.mTvFileDes).text = "${data?.getFileType()} | ${data?.fileSize}\n${data?.lastModified}"

        val type = FileUtils.getFileTypeForUrl(data?.path)
        when (type) {
            FileType.PDF -> {
                itemView.findViewById<CardView>(R.id.mCvDocCell).setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.findViewById<CardView>(R.id.mCvDocCell).resources,
                        R.color.listItemColorPdf,
                        itemView.findViewById<CardView>(R.id.mCvDocCell).context.theme
                    )
                )
            }
            FileType.DOC,FileType.DOCX -> {
                itemView.findViewById<CardView>(R.id.mCvDocCell).setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.findViewById<CardView>(R.id.mCvDocCell).resources,
                        R.color.listItemColorDoc,
                        itemView.findViewById<CardView>(R.id.mCvDocCell).context.theme
                    )
                )
            }
            FileType.XLS,FileType.XLSX -> {
                itemView.findViewById<CardView>(R.id.mCvDocCell).setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.findViewById<CardView>(R.id.mCvDocCell).resources,
                        R.color.listItemColorExcel,
                        itemView.findViewById<CardView>(R.id.mCvDocCell).context.theme
                    )
                )
            }
            FileType.PPT,FileType.PPTX -> {
                itemView.findViewById<CardView>(R.id.mCvDocCell).setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.findViewById<CardView>(R.id.mCvDocCell).resources,
                        R.color.listItemColorPPT,
                        itemView.findViewById<CardView>(R.id.mCvDocCell).context.theme
                    )
                )
            }
            FileType.IMAGE -> {
                itemView.findViewById<CardView>(R.id.mCvDocCell).setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.findViewById<CardView>(R.id.mCvDocCell).resources,
                        R.color.listItemColorImage,
                        itemView.findViewById<CardView>(R.id.mCvDocCell).context.theme
                    )
                )
            }
        }
    }

    override fun onClick(v: View?) {
        mOnItemClickListener?.onItemClick(null,v,adapterPosition,parentPosition.toLong())
    }

}