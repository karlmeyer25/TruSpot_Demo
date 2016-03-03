package com.truspot.android.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;

import android.graphics.drawable.Drawable;

public class PdmDrawable extends Drawable {

    // variables
    private int mColor;
    private int mBorderWidth;
    private int mBorderRadius;

    // graphic variables
    private Paint mPaint;
    private RectF mInnerRect;
    private RectF mOuterRect;
    private Path mPath;

    public PdmDrawable(int color, int borderWidth, int borderRadius) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
        mPath.setFillType(Path.FillType.EVEN_ODD);

        mInnerRect = new RectF();
        mOuterRect = new RectF();

        mColor = color;
        mBorderWidth = borderWidth;
        mBorderRadius = borderRadius;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mPath.reset();

        mInnerRect.set(bounds.left,
                bounds.top,
                bounds.right,
                bounds.bottom);
        mPath.addOval(mInnerRect, Path.Direction.CW);

        mOuterRect.set(bounds.left + mBorderWidth,
                bounds.top + mBorderWidth,
                bounds.right - mBorderWidth,
                bounds.bottom - mBorderWidth);
        mPath.addOval(mOuterRect, Path.Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(mColor);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
