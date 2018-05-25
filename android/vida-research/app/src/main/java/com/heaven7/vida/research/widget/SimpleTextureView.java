package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.heaven7.core.util.Logger;

/**
 * Created by heaven7 on 2018/5/25 0025.
 */

public class SimpleTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String TAG = "SimpleTextureView";
    private Surface mSurface;
    private Callback mCallback;

    public interface Callback{
        void initSurface(Surface surface, int width, int height);
        void onSizeChanged(SurfaceTexture surface, int width, int height);
    }

    public SimpleTextureView(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(!isInEditMode()) {
            setSurfaceTextureListener(this);
        }
    }
    public Callback getCallback() {
        return mCallback;
    }
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    //=======================================================

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSurfaceIfNeed();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if((getSurfaceTexture() != null && getSurfaceTexture() != surface) || mSurface == null) {
            releaseSurfaceIfNeed();
            mSurface = new Surface(surface);
        }
        if(getCallback() != null) {
            getCallback().initSurface(mSurface, width, height);
        }else{
            Logger.w(TAG, "onSurfaceTextureAvailable", "mCallback is null.");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if(getCallback() != null) {
            getCallback().onSizeChanged(surface, width, height);
        }else{
            Logger.w(TAG, "onSurfaceTextureAvailable", "mCallback is null.");
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseSurfaceIfNeed();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    private void releaseSurfaceIfNeed() {
        if(mSurface != null && mSurface.isValid()){
            mSurface.release();
            mSurface = null;
        }
    }
}
