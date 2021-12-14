package com.example.downloaddemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downloaddemo.R
import com.example.downloaddemo.data.ItemData

class SelectAdapter(context:Context, private val exampleList: List<ItemData>) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
    private val mInflater = LayoutInflater.from(context)
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.tv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.layout_recycle_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data1 = exampleList[position]
        holder.textView.text = data1.text
    }

    override fun getItemCount() = exampleList.size
}