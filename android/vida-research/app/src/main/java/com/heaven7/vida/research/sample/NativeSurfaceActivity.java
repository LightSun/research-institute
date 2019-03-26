package com.heaven7.vida.research.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.SimpleTextureView;

import java.io.ByteArrayOutputStream;

/**
 * Created by heaven7 on 2018/5/25 0025.
 */

public class NativeSurfaceActivity extends AppCompatActivity {

    private SimpleTextureView mTextureView;
    private NativeSurfaceTest mNst = new NativeSurfaceTest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_native_surface);

        mTextureView = findViewById(R.id.textureView);
        mTextureView.setCallback(mNst);
    }

    public void doDraw(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoliyin);
       /* bitmap.setHasAlpha(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);*/

       // mNst.draw(baos.toByteArray());
        mNst.drawBitmap(bitmap);
    }
}
