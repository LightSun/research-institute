package com.semantive.waveformandroid.waveform.view;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by heaven7 on 2019/5/17.
 */
/*public*/ interface EditorWaveformCallback {

    RectF getContentRect();

    boolean isSelected();

    RectF getTmpRectF();

    Paint getFocusPaint();

    float getTruncateTailWidth();
}
