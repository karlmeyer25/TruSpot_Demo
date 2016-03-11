package com.truspot.android.animations;

import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ViewMover {
    // variables
    private List<MoverObject> objectsToMove;
    private int animDuration;
    private boolean isDefaultPosition = true;
    private TimeInterpolator interpolator;

    public ViewMover(List<MoverObject> objectsToMove, int animDuration) {
        this(objectsToMove, null, animDuration);
    }

    public ViewMover(int animDuration) {
        this(null, null, animDuration);
    }

    public ViewMover(TimeInterpolator interpolator, int animDuration) {
        this(null, interpolator, animDuration);
    }

    public ViewMover(List<MoverObject> objectsToMove, TimeInterpolator interpolator, int animDuration) {
        this.objectsToMove = objectsToMove;
        this.interpolator = interpolator != null ? interpolator : new LinearInterpolator();
        this.animDuration = animDuration;
    }

    public ViewMover addObjectToMove(MoverObject object) {
        if (objectsToMove == null) {
            objectsToMove = new ArrayList<>();
        }

        objectsToMove.add(object);

        return this;
    }

    public void move(boolean isDefaultPosition, boolean withAnimation) {
        if (this.isDefaultPosition != isDefaultPosition) {
            this.isDefaultPosition = isDefaultPosition;

            if (Util.isListNotEmpty(objectsToMove)) {
                for (MoverObject moverObject : objectsToMove) {
                    int translationX = isDefaultPosition ? moverObject.defaultLeft : moverObject.desiredLeft;
                    int translationY = isDefaultPosition ? moverObject.defaultTop : moverObject.desiredTop;

                    if (withAnimation) {
                        moverObject.
                                view.
                                animate().
                                setInterpolator(interpolator).
                                setDuration(animDuration).
                                translationX(translationX).
                                translationY(translationY);
                    } else {
                        View view = moverObject.view;

                        view.
                                setTranslationX(
                                        //view.getTranslationX() + translationXBy
                                        translationX
                                );

                        view.
                                setTranslationY(
                                        //view.getTranslationY() + translationYBy
                                        translationY
                                );
                    }
                }
            }
        }
    }

    public void toggleMove(boolean withAnimation) {
        move(!isDefaultPosition, withAnimation);
    }

    public boolean isDefaultPosition() {
        return isDefaultPosition;
    }

    // inner classes
    public static class MoverObject {
        View view;
        int defaultTop;
        int defaultLeft;
        int desiredTop;
        int desiredLeft;

        public MoverObject(View view, int defaultTop, int defaultLeft, int desiredTop, int desiredLeft) {
            this.view = view;
            this.defaultTop = defaultTop;
            this.defaultLeft = defaultLeft;
            this.desiredTop = desiredTop;
            this.desiredLeft = desiredLeft;
        }
    }
}
