package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by heaven7 on 2019/5/16.
 */
public class MusicStartEndView extends WaveformView {

    public MusicStartEndView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new MusicStartEndDrawDelegate(this));
        setFixSelectLength(true);
    }

    @Override
    protected void onPreDraw() {
        mMinOffsetX = -getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO draw str.
    }
}
