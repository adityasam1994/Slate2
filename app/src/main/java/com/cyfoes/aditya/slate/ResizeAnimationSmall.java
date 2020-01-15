package com.cyfoes.aditya.slate;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class ResizeAnimationSmall extends Animation {
    LinearLayout drawerlayout;
    int startHeight;
    final int targetHeight;

    public boolean willChangeBounds() {
        return true;
    }

    public ResizeAnimationSmall(LinearLayout linearLayout, int i, int i2) {
        this.drawerlayout = linearLayout;
        this.targetHeight = i;
        this.startHeight = i2;
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float f, Transformation transformation) {
        this.drawerlayout.getLayoutParams().height = (int) (((float) this.startHeight) - (((float) (this.startHeight - this.targetHeight)) * f));
        this.drawerlayout.requestLayout();
    }

    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
    }
}
