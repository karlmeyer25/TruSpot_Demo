package com.truspot.android.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;

import android.graphics.drawable.Drawable;

public class PdmDrawable extends Drawable {

    // variables
    private int mBorderColor;
    private int mDensityColor;
    private int mBorderWidth;

    // graphic variables
    private Paint mBorderPaint;
    private Paint mDensityPaint;
    private RectF mBorderCircle;
    private RectF mDensityCircle;

    public PdmDrawable(int borderColor, int densityColor, int borderWidth) {
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.FILL);

        mDensityPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDensityPaint.setStyle(Paint.Style.FILL);

        mBorderCircle = new RectF();
        mDensityCircle = new RectF();

        mBorderColor = borderColor;
        mDensityColor = densityColor;

        mBorderWidth = borderWidth;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mBorderCircle.set(bounds.left,
                bounds.top,
                bounds.right,
                bounds.bottom);

        mDensityCircle.set(bounds.left + mBorderWidth,
                bounds.top + mBorderWidth,
                bounds.right - mBorderWidth,
                bounds.bottom - mBorderWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        mBorderPaint.setColor(mBorderColor);
        mDensityPaint.setColor(mDensityColor);
        canvas.drawOval(mBorderCircle, mBorderPaint);
        canvas.drawOval(mDensityCircle, mDensityPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mBorderPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBorderPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
