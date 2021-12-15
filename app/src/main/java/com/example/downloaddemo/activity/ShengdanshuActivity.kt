package com.example.downloaddemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.downloaddemo.R

class ShengdanshuActivity: Activity() {
    companion object{
        @JvmStatic fun startActivity(context: Context){
            val intent = Intent(context,ShengdanshuActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shengdanshu)
    }
}