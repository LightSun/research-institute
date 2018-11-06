package com.heaven7.vida.research.drawable;

import android.graphics.drawable.Drawable;

/**
 * Created by heaven7 on 2018/11/6 0006.
 */
public interface TransDrawableDelegate {

    /**
     * set the background color
     * @param color the color
     */
    void setBackgroundColor(int color);

    /**
     * set the size of trans
     * @param size the size in pixes.
     */
    void setSize(int size);

    /**
     * set the mini drawable which will draw in center
     * @param d the drawable.
     */
    void setMiniDrawable(Drawable d);

}
