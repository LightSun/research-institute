package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by heaven7 on 2019/5/16.
 */
public class EditorWaveformView extends WaveformView {

    public EditorWaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new UpWaveformDrawDelegate(this));
        mAP.startDy = 30;
    }
}
