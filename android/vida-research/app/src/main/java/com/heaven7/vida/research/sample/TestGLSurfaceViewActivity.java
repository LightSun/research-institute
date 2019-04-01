package com.heaven7.vida.research.sample;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.outline.RoundRectOutlineProvider;
import com.heaven7.vida.research.utils.DimenUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.OnClick;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;

/**
 * GLSurfaceView 绘制的可见性切换
 * Created by heaven7 on 2019/3/28.
 */
public class TestGLSurfaceViewActivity extends BaseActivity {

    private final GLRender mRender = new GLRender();

    @BindView(R.id.gl)
    GLSurfaceView mGlSurfaceView;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_gl_surface;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
       // setTranslucent();
        mGlSurfaceView.setEGLContextClientVersion(3);
        mGlSurfaceView.setRenderer(mRender);

        mGlSurfaceView.setOutlineProvider(new RoundRectOutlineProvider(0, DimenUtil.dip2px(context, 20)));
        mGlSurfaceView.setClipToOutline(true);
    }

    @OnClick(R.id.btn_toggle)
    public void onClickToggle(View view){
        mRender.toggle();
    }

    public void setTranslucent() {
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGlSurfaceView.setZOrderOnTop(true);
    }

    private static class GLRender implements GLSurfaceView.Renderer{

        boolean show;
        public void setShow(boolean show) {
            this.show = show;
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }
        @Override
        public void onDrawFrame(GL10 gl) {
            if(show){
                gl.glClearColor(1f,0f,0f ,1f);
            }else {
                gl.glClearColor(0f,0f,0f ,0f);
            }
            gl.glClear(GL_COLOR_BUFFER_BIT);
        }
        public void toggle() {
            show =!show;
        }
    }
}
