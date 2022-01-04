package com.example.downloaddemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downloaddemo.R
import com.example.downloaddemo.data.ItemData

class SelectAdapter(context:Context) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
    private val mInflater = LayoutInflater.from(context)
    private val logs = mutableListOf<ItemData>()
    private var maxLogNum = 10000;
    private var lPosition = 0;


    fun addLog(item: ItemData){
        lPosition++
        if (logs.size>maxLogNum){
            logs.removeAt(0)
        }
        item.lineNum = lPosition
        logs.add(item)
        notifyDataSetChanged()
    }

    fun clear(){
        logs.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.tv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.layout_recycle_item,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data1 = logs.get(position)
        if (data1.value==1){
            holder.textView.setTextColor(Color.BLACK)
        }else if(data1.value==2){
            holder.textView.setTextColor(Color.BLUE)
        }else if (data1.value==3){
            holder.textView.setTextColor(Color.RED)
        }

        holder.textView.text = data1.lineNum.toString() + " :"+data1.text
    }

    override fun getItemCount() = logs.size
}