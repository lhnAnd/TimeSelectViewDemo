package com.example.downloaddemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downloaddemo.R
import com.example.downloaddemo.adapter.SelectAdapter
import com.example.downloaddemo.data.ItemData

class KtTestActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kt_test_layout)
        initView()
    }

    private fun initView(){
        val backButton = findViewById<Button>(R.id.bn_back)
        backButton.setOnClickListener { finish() }
        val myRecycler = findViewById<RecyclerView>(R.id.my_recycler_view)
        val exampleList = mutableListOf<ItemData>()
        for (index in 1..100){
            val data = ItemData(""+index,index)
            exampleList.add(data)
        }
        myRecycler.adapter = SelectAdapter(this,exampleList)
        myRecycler.layoutManager = LinearLayoutManager(this)
    }

    companion object{
        @JvmStatic fun startActivity(context:Context){
            val intent = Intent()
            intent.setClass(context,KtTestActivity::class.java);
            context.startActivity(intent)
        }
    }
}