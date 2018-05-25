package com.heaven7.vida.research.sample;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;

import com.heaven7.vida.research.widget.SimpleTextureView;

/**
 * Created by heaven7 on 2018/5/25 0025.
 */

public class NativeSurfaceTest implements SimpleTextureView.Callback {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void initSurface(Surface surface, int width, int height) {
        setSurface(surface, width, height);
    }
    @Override
    public void onSizeChanged(SurfaceTexture surface, int width, int height) {
        updateFrame(width, height);
    }

    public native void draw(byte[] imageBits);
    public native void drawBitmap(Bitmap bitmap);

    private native void updateFrame(int width, int height);

    private native void setSurface(Surface surface, int width, int height);

    private native void nDestroy();

    @Override
    protected void finalize() throws Throwable {
        nDestroy();
        super.finalize();
    }

}
