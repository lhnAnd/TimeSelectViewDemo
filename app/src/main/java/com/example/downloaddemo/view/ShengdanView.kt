package com.example.downloaddemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShengdanView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val star1 = arrayOf(100f,100f)
    private val star2 = arrayOf(180f,600f)
    private val jindu = 0

    private val paint = Paint()
    init {
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 5F
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(star1[0],star1[1],star2[0],star2[1],paint)
    }
}