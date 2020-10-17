package com.hencoder.a16_layout.guo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.ViewCompat
import com.hencoder.a16_layout.R

class FlowLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var singleLine: Boolean = false
    private var lineSpacing: Int = 0
    private var itemSpacing: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        itemSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_itemSpacing, 0)
        lineSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_lineSpacing, 0)
        singleLine = a.getBoolean(R.styleable.FlowLayout_isSingle, false)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val maxWidth = if (widthMode == MeasureSpec.UNSPECIFIED) Int.MAX_VALUE else width
        val maxRight = maxWidth - paddingRight

        var childLeft = paddingLeft
        var childTop = paddingTop
        var childRight = childLeft
        var childBottom = childTop

        var maxChildRight = 0
        var maxChildBottom = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            var leftMargin = 0
            var rightMargin = 0
            val lp = child.layoutParams
            if (lp is MarginLayoutParams) {
                leftMargin = lp.leftMargin
                rightMargin = lp.rightMargin
            }
            childRight = childLeft + leftMargin + child.measuredWidth

            if (childRight > maxRight && !singleLine) {
                childLeft = paddingLeft
                childTop = maxChildBottom + lineSpacing
            }

            childRight = childLeft + leftMargin + child.measuredWidth
            childBottom = childTop + child.measuredHeight

            if (childBottom > maxChildBottom) {
                maxChildBottom = childBottom
            }

            if (childRight > maxChildRight) {
                maxChildRight = childRight
            }
            childLeft += (leftMargin + rightMargin + child.measuredWidth) + itemSpacing
            if (i == childCount - 1) {
                maxChildRight += rightMargin
            }
        }
        maxChildRight += paddingRight
        childBottom += paddingBottom
        val finalWidth = getMeasuredDimension(width, widthMode, maxChildRight)
        val finalHeight = getMeasuredDimension(height, heightMode, maxChildBottom)
        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount == 0) {
            return
        }

        val isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        val paddingStart = if (isRtl) paddingRight else paddingLeft
        val paddingEnd = if (isRtl) paddingLeft else paddingRight

        var childStart = paddingStart
        var childTop = paddingTop
        var childBottom = childTop
        var childEnd: Int

        val maxChildEnd = right - left - paddingEnd
        var maxChildHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (child.visibility == View.GONE) {
                continue
            }

            val lp = child.layoutParams
            var startMargin = 0
            var endMargin = 0
            if (lp is MarginLayoutParams) {
                startMargin = MarginLayoutParamsCompat.getMarginStart(lp)
                endMargin = MarginLayoutParamsCompat.getMarginEnd(lp)
            }

            childEnd = childStart + startMargin + child.measuredWidth
            if (!singleLine && (childEnd > maxChildEnd)) {
                childStart = paddingStart
                childTop = childBottom + lineSpacing
                maxChildHeight = 0
            }

            childEnd = childStart + startMargin + child.measuredWidth
            if (child.measuredHeight > maxChildHeight) {
                maxChildHeight = child.measuredHeight
            }
            childBottom = childTop + maxChildHeight

            if (isRtl) {
                child.layout(maxChildEnd - childEnd, childTop, maxChildEnd - childStart - startMargin, childTop + child.measuredHeight)
            } else {
                child.layout(childStart + startMargin, childTop, childEnd, childTop + child.measuredHeight)
            }

            childStart += (startMargin + endMargin + child.measuredWidth) + itemSpacing
        }
    }

    companion object {
        private fun getMeasuredDimension(size: Int, model: Int, childrenEdge: Int): Int {
            return when (model) {
                MeasureSpec.EXACTLY -> size
                MeasureSpec.AT_MOST -> childrenEdge.coerceAtMost(size)
                else -> childrenEdge
            }
        }
    }
}