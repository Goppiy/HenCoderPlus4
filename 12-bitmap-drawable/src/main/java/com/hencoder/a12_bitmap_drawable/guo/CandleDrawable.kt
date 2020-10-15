package com.hencoder.a12_bitmap_drawable.guo

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import com.hencoder.a12_bitmap_drawable.Utils

class CandleDrawable() : Drawable() {

    private val topLineHeight = Utils.dp2px(10F)
    private val bottomLineHeight = Utils.dp2px(16F)
    private var candleHeight = Utils.dp2px(50F)

    private val lineWidth = Utils.dp2px(2.5F)

    private var status = Status.UP

    private val paint = Paint().apply {
        color = if (status == Status.UP) Color.RED else Color.GREEN
    }

    override fun draw(canvas: Canvas) {
        val candleWidth = bounds.width()
        val lineStartX = candleWidth / 2F
        // draw top line
        var startY = 0F
        var stopY = topLineHeight
        paint.strokeWidth = lineWidth
        canvas.drawLine(lineStartX, startY, lineStartX, stopY, paint)

        // 画中间的实体
        startY = stopY
        stopY += candleHeight
        canvas.drawRect(0F, startY, candleWidth.toFloat(), stopY, paint)

        // draw bottom line
        startY = stopY
        stopY += bottomLineHeight
        canvas.drawLine(lineStartX, startY, lineStartX, stopY, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? {
        return paint.colorFilter
    }

    override fun getOpacity(): Int {
        return when (paint.alpha) {
            0 -> PixelFormat.TRANSPARENT
            0xff -> PixelFormat.OPAQUE
            else -> PixelFormat.TRANSLUCENT
        }
    }

    fun updateStatus(status: Status) {
        this.status = status
        paint.color = getColor()
        invalidateSelf()
    }

    fun getDrawableHeight(): Float {
        return topLineHeight + candleHeight + bottomLineHeight
    }

    private fun getColor(): Int {
        return if (status == Status.UP) Color.RED else Color.GREEN
    }

    enum class Status {
        UP, DOWN
    }
}