package com.hencoder.a16_layout.guo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class MeasureSpecView extends androidx.appcompat.widget.AppCompatTextView {

    public MeasureSpecView(Context context) {
        super(context);
    }

    public MeasureSpecView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("MeasureSpec", "widthMode: " + getMode(widthMode) + " widthSize: " + widthSize);
        Log.d("MeasureSpec", "heightMode: " + getMode(heightMode) + " heightSize: " + heightSize);
    }

    private String getMode(int mode) {
        if (mode == MeasureSpec.AT_MOST) {
            return "AT_MOST";
        } else if (mode == MeasureSpec.EXACTLY) {
            return "EXACTLY";
        } else {
            return "UNSPECIFIED";
        }
    }
}
