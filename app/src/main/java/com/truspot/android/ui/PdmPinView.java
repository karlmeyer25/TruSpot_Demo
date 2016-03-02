package com.truspot.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.truspot.android.R;

public class PdmPinView extends ImageView {

    // variables
    private PdmDrawable mBorder;

    public PdmPinView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public PdmPinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        setWillNotDraw(false);
        mBorder = new PdmDrawable(context.getResources().getColor(R.color.pdm_border),
                getPaddingLeft(), getPaddingLeft() / 2);

        setImageDrawable(context.getResources().getDrawable(R.drawable.pdm));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBorder.setBounds(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBorder.draw(canvas);
    }
}
