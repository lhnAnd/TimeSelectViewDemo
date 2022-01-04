package com.example.downloaddemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.example.downloaddemo.R
import com.example.downloaddemo.adapter.SelectAdapter
import com.example.downloaddemo.data.ItemData
import com.example.downloaddemo.event.MessageEvent2
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LogActivity: Activity() {
    lateinit var myAdapter : SelectAdapter
    companion object{
        @JvmStatic fun startActivity(context: Context){
            context.startActivity(Intent(context,LogActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        val recyclerView = findViewById<RecyclerView>(R.id.log_recycle)
        myAdapter = SelectAdapter(this)
        recyclerView.adapter = myAdapter
        findViewById<Button>(R.id.finish).setOnClickListener { super.finish() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        EventBus.getDefault().register(this)
        if (MainActivity.logActivity == null){
            MainActivity.logActivity = this
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event:MessageEvent2) {
        if (event.num == 10){
            var message = event.message;
            myAdapter.addLog(ItemData(event.message,0))
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun finish() {
//        super.finish()
    }
}
