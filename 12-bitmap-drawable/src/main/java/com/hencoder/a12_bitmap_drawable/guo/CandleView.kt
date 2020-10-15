package com.hencoder.a12_bitmap_drawable.guo

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.hencoder.a12_bitmap_drawable.Utils

class CandleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val background: CandleDrawable = CandleDrawable().apply {
        updateStatus(CandleDrawable.Status.DOWN)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        background.setBounds(0, 0, measuredWidth, measuredHeight)

        background.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(Utils.dp2px(12F).toInt(), background.getDrawableHeight().toInt())
    }
}