package com.semantive.waveformandroid.waveform.view;

/*public*/ class WaveformParam{

    int selectStrokeWidth;
    /** the round size of whole waveform */
    int roundSize;

    int minOffsetX;
    int offsetX;
    int width; //valid width of waveform
    int viewWidth;
    int viewHeight;

    //in pixes
    int selectionStart;
    int selectionEnd;

    double timecodeIntervalSecs;
    double fractionalSecs;
    double onePixelInSecs;

}