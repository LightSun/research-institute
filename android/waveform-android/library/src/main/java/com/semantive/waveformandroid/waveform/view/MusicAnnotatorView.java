package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by heaven7 on 2019/5/15.
 */
public class MusicAnnotatorView extends WaveformView2 {

    public MusicAnnotatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new UpDownWaveformDrawDelegate(this));
    }


}
