package com.heaven7.vida.research.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by heaven7 on 2018/11/6 0006.
 */
public class VidaTransDrawable extends Drawable implements TransDrawableDelegate{

    private final State mState;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    private boolean mColorFilterSet;

    public VidaTransDrawable(State mState) {
        this.mState = mState;
        mPaint.setStyle(Paint.Style.FILL);
    }
    public VidaTransDrawable() {
        this(new State());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, mPaint);
        //start draw mini drawable.
        final Drawable d = mState.miniDrawable;
        if(mState.alpha >= 0){
            d.setAlpha(mState.alpha);
        }
       /* if(mState.filter != null){
            d.setColorFilter(mState.filter);
        }*/
        int x = (bounds.width() - d.getIntrinsicWidth()) / 2;
        int y = (bounds.height() - d.getIntrinsicHeight()) / 2;
        canvas.translate(x, y);
        d.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mState.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public int getIntrinsicHeight() {
        return getBounds().height();
    }
    @Override
    public int getIntrinsicWidth() {
        return getBounds().width();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        mState.filter = colorFilter;
        invalidateSelf();
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        mState.mTint = tint;
        updateTint(getState());
        invalidateSelf();
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mState.mTintMode = tintMode;
        updateTint(getState());
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
        mState.backColor = color;
        invalidateSelf();
    }

    @Override
    public void setSize(int size) {
        setBounds(0, 0, size, size);
    }

    @Override
    public void setMiniDrawable(Drawable d) {
        mState.miniDrawable = d;
        d.setBounds(0, 0 , d.getIntrinsicWidth(), d.getIntrinsicHeight());
        invalidateSelf();
    }

    private boolean updateTint(int[] state) {
        if (Build.VERSION.SDK_INT < 21) {
            // If compat tinting is not enabled, fail fast
            return false;
        }

        final ColorStateList tintList = mState.mTint;
        final PorterDuff.Mode tintMode = mState.mTintMode;

        if (tintList != null && tintMode != null) {
            final int color = tintList.getColorForState(state, tintList.getDefaultColor());
            if (!mColorFilterSet || color != mCurrentColor || tintMode != mCurrentMode) {
                setColorFilter(color, tintMode);
                mCurrentColor = color;
                mCurrentMode = tintMode;
                mColorFilterSet = true;
                return true;
            }
        } else {
            mColorFilterSet = false;
            clearColorFilter();
        }
        return false;
    }

    public static class State{
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;
        ColorFilter filter;
        int alpha = -1;

        int backColor;
        Drawable miniDrawable;

        public State(){}
        public State(State src) {
            this.mTint = src.mTint;
            this.mTintMode = src.mTintMode;
            this.filter = src.filter;
            this.alpha = src.alpha;
            this.backColor = src.backColor;
            this.miniDrawable = src.miniDrawable;
        }
    }
}
