package com.hencoder.a12_bitmap_drawable.guo

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.hencoder.a12_bitmap_drawable.R
import com.hencoder.a12_bitmap_drawable.Utils

class MaterialEditText2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private val TEXT_SIZE = Utils.dp2px(12f)
    private val TEXT_MARGIN = Utils.dp2px(8f)
    private val VERTICAL_OFFSET = Utils.dp2px(38f)
    private val HORIZONTAL_OFFSET = Utils.dp2px(5f)
    private val EXTRA_OFFSET = Utils.dp2px(16f)

    private var floatingLabelShown = false
    private var floatingFraction: Float = 0F
    private var useFloatingLabel = true

    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "floatingFraction", 1F)
    }

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText2)
        useFloatingLabel = a.getBoolean(R.styleable.MaterialEditText2_useFloatingLabel2, true)
        a.recycle()

        paint.textSize = textSize

        refreshPadding()

        doAfterTextChanged {
            if (useFloatingLabel) {
                if (!floatingLabelShown && !TextUtils.isEmpty(it)) {
                    floatingLabelShown = true
                    animator.start()
                } else if (floatingLabelShown && TextUtils.isEmpty(it)) {
                    floatingLabelShown = false
                    animator.reverse()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (useFloatingLabel) {
            paint.alpha = (floatingFraction * 0xff).toInt()
            val offset = -floatingFraction * EXTRA_OFFSET
            canvas?.drawText(hint.toString(), HORIZONTAL_OFFSET, VERTICAL_OFFSET + offset, paint)
        }
    }

    fun setFloatingFraction(fraction: Float) {
        floatingFraction = fraction
        invalidate()
    }

    fun getFloatingFraction() : Float {
        return floatingFraction
    }

    private fun refreshPadding() {
        val rect = Rect()
        background.getPadding(rect)
        if (useFloatingLabel) {
            setPadding(rect.left, (rect.top + TEXT_SIZE + TEXT_MARGIN).toInt(), rect.right, rect.bottom)
        } else {
            setPadding(rect.left, rect.top, rect.right, rect.bottom)
        }
    }
}