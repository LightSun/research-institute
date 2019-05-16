package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

/**
 * Created by heaven7 on 2019/5/15.
 */
//播放的时候加动画。
public class MusicAnnotatorView extends WaveformView {

    public MusicAnnotatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new UpDownWaveformDrawDelegate(this));

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mMinOffsetX = -getWidth() / 2;
                return true;
            }
        });
    }


}
