package com.cherry.doc

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cherry.doc.data.DocGroupInfo

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

class DocViewHolder : RecyclerView.ViewHolder,OnClickListener {
    var mOnItemClickListener: OnItemClickListener? = null
    constructor(itemView: View) : super(itemView) {
        itemView.setOnClickListener(this)
    }

    fun bindData(data: DocGroupInfo?) {
        itemView.findViewById<TextView>(R.id.mTvTypeName).text = data?.typeName

//        itemView.mRvDocCell.onFlingListener = null
//        LinearSnapHelper().attachToRecyclerView(itemView.mRvDocCell)

        var cellAdapter = DocCellAdapter(itemView.context,mOnItemClickListener,
            adapterPosition)
        cellAdapter.showDatas(data?.docList)

        itemView.findViewById<RecyclerView>(R.id.mRvDocCell).adapter = cellAdapter
    }

    override fun onClick(v: View?) {
        mOnItemClickListener?.onItemClick(null,v,adapterPosition,0)
    }

}