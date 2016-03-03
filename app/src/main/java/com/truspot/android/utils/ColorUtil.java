package com.truspot.android.utils;

import android.graphics.Color;

/**
 * Created by yavoryordanov on 3/3/16.
 */
public class ColorUtil {

    public static int adjustAlpha(String color, float factor) {
        return adjustAlpha(Color.parseColor(color), factor);
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }
}
