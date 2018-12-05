package com.heaven7.android.sample;

import android.meta.cts.EncodeAndMuxTest;
import android.view.View;

/**
 * Created by heaven7 on 2018/12/5 0005.
 */
public class TestEncodeAndMuxActivity extends TestMediaCodecAudioTrackActivity {

    private EncodeAndMuxTest sample = new EncodeAndMuxTest();

    @Override
    public void onClickStart(View view) {
       // super.onClickStart(view);
        sample.testEncodeVideoToMp4();
    }
}
