package com.cherry.doc

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cherry.doc.data.DocInfo
import com.cherry.doc.R
import com.nvqquy98.lib.doc.bean.FileType
import com.nvqquy98.lib.doc.util.FileUtils
import kotlinx.android.synthetic.main.rv_doc_item_cell.view.*
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
                itemView.mIvType.load(File(data?.path))
            } else {
                itemView.mIvType.load(com.nvqquy98.lib.doc.R.drawable.all_doc_ic)
            }
        } else {
            itemView.mIvType.load(typeIcon)
        }
        itemView.mTvFileName.text = data?.fileName
        itemView.mTvFileDes.text = "${data?.getFileType()} | ${data?.fileSize}\n${data?.lastModified}"

        val type = FileUtils.getFileTypeForUrl(data?.path)
        when (type) {
            FileType.PDF -> {
                itemView.mCvDocCell.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.mCvDocCell.resources,
                        R.color.listItemColorPdf,
                        itemView.mCvDocCell.context.theme
                    )
                )
            }
            FileType.DOC,FileType.DOCX -> {
                itemView.mCvDocCell.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.mCvDocCell.resources,
                        R.color.listItemColorDoc,
                        itemView.mCvDocCell.context.theme
                    )
                )
            }
            FileType.XLS,FileType.XLSX -> {
                itemView.mCvDocCell.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.mCvDocCell.resources,
                        R.color.listItemColorExcel,
                        itemView.mCvDocCell.context.theme
                    )
                )
            }
            FileType.PPT,FileType.PPTX -> {
                itemView.mCvDocCell.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.mCvDocCell.resources,
                        R.color.listItemColorPPT,
                        itemView.mCvDocCell.context.theme
                    )
                )
            }
            FileType.IMAGE -> {
                itemView.mCvDocCell.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        itemView.mCvDocCell.resources,
                        R.color.listItemColorImage,
                        itemView.mCvDocCell.context.theme
                    )
                )
            }
        }
    }

    override fun onClick(v: View?) {
        mOnItemClickListener?.onItemClick(null,v,adapterPosition,parentPosition.toLong())
    }

}
