package com.cherry.doc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.cherry.doc.data.DocGroupInfo
import com.cherry.doc.data.DocInfo

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DocAdapter
 * Author: Victor
 * Date: 2023/10/26 10:56
 * Description: 
 * -----------------------------------------------------------------
 */

class DocAdapter(var context: Context, var listener: AdapterView.OnItemClickListener)
    : RecyclerView.Adapter<DocViewHolder>() {

    var datas = ArrayList<DocGroupInfo>()

    fun showDatas(docList: ArrayList<DocGroupInfo>) {
        datas.clear()
        datas.addAll(docList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        return DocViewHolder(inflate(R.layout.rv_doc_cell,parent))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        holder.mOnItemClickListener = listener
        holder.bindData(datas[position])
    }

    fun inflate(layoutId: Int,parent: ViewGroup): View {
        var inflater = LayoutInflater.from(context)
        return inflater.inflate(layoutId,parent, false)
    }
}