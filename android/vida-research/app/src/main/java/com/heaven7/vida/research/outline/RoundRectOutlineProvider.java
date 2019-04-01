package com.heaven7.vida.research.outline;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

@TargetApi(21)
public class RoundRectOutlineProvider extends ViewOutlineProvider {

    private final int margin;
    private final float radius;

    public RoundRectOutlineProvider(int margin, float radius) {
        this.margin = margin;
        this.radius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(margin, margin,
                view.getWidth() - margin,
                view.getHeight() - margin,
                radius);
    }
}
