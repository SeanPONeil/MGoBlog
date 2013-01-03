package com.atami.mgodroid.ui.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ThreePaneLayout extends LinearLayout {
    private static final int ANIM_DURATION = 500;
    private View left = null;
    private View middle = null;
    private View right = null;
    private int leftWidth = -1;
    private int middleWidthNormal = -1;

    public ThreePaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf();
    }

    void initSelf() {
        setOrientation(HORIZONTAL);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        left = getChildAt(0);
        middle = getChildAt(1);
        right = getChildAt(2);
    }

    public View getLeftView() {
        return (left);
    }

    public View getMiddleView() {
        return (middle);
    }

    public View getRightView() {
        return (right);
    }

    public void hideLeft() {
        if (leftWidth == -1) {
            leftWidth = left.getWidth();
            middleWidthNormal = middle.getWidth();
            resetWidget(left, leftWidth);
            resetWidget(middle, middleWidthNormal);
            resetWidget(right, middleWidthNormal);
            requestLayout();
        }

        translateWidgets(-1 * leftWidth, left, middle, right);

        ObjectAnimator.ofInt(this, "middleWidth", middleWidthNormal,
                leftWidth).setDuration(ANIM_DURATION).start();
    }

    public void showLeft() {
        translateWidgets(leftWidth, left, middle, right);

        ObjectAnimator.ofInt(this, "middleWidth", leftWidth,
                middleWidthNormal).setDuration(ANIM_DURATION)
                .start();
    }

    public void setMiddleWidth(int value) {
        middle.getLayoutParams().width = value;
        requestLayout();
    }

    private void translateWidgets(int deltaX, View... views) {
        for (final View v : views) {
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            v.animate().translationXBy(deltaX).setDuration(ANIM_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            v.setLayerType(View.LAYER_TYPE_NONE, null);
                        }
                    });
        }
    }

    private void resetWidget(View v, int width) {
        LinearLayout.LayoutParams p =
                (LinearLayout.LayoutParams) v.getLayoutParams();

        p.width = width;
        p.weight = 0;
    }
}
