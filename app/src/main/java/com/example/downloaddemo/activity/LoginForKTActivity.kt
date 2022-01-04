package com.example.downloaddemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnFocusChange
import com.example.downloaddemo.R
import com.google.android.material.textfield.TextInputEditText


class LoginForKTActivity : Activity() {

    @BindView(R.id.account) lateinit var inputAccount : TextInputEditText
    @BindView(R.id.password) lateinit var inputPw : TextInputEditText
//    @BindView(R.id.iv_clear) lateinit var ivClear : ImageView

    companion object{
        val TAG = this.javaClass.simpleName;
        @JvmStatic fun startActivity(context: Context){
            context.startActivity(Intent(context,LoginForKTActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_kt)
        ButterKnife.bind(this)
        val ivClear = findViewById<ImageView>(R.id.iv_clear)
        ivClear.setOnClickListener {
            inputPw.setText("")
            ivClear.visibility = View.GONE
        }
        inputPw.setOnFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

            }
        } )
        inputPw.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                Log.d(TAG,inputPw.text.toString())
                Log.d(TAG,"inputPw.text.toString()")
//                if (event.)
//                inputPw.text.toString() == ""
                return true
            }
        })
    }
}