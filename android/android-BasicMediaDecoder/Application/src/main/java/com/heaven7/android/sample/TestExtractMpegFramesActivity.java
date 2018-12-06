package com.heaven7.android.sample;

import android.content.Context;
import android.meta.cts.ExtractMpegFramesTest;
import android.os.Bundle;
import android.view.View;

/**
 * 提取一个.mp4视频文件的开始10帧，并保持成一个PNG文件到sd卡中，
 * 使用 MediaExtractor 提取 CSD 数据，
 * 并将单个 access units给 MediaCodec 解码器，
 * 帧被解码到一个SurfaceTexture的surface中，离屏渲染，并通过 glReadPixels() 拿到数据后使用 Bitmap#compress() 保存成一个PNG 文件。
 * Created by heaven7 on 2018/12/6 0006.
 */
public class TestExtractMpegFramesActivity extends TestMediaCodecAudioTrackActivity {

    private ExtractMpegFramesTest mTest;

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        super.onInitialize(context, savedInstanceState);
        mTest = new ExtractMpegFramesTest();
    }

    @Override
    public void onClickStart(View view) {
        try {
            mTest.testExtractMpegFrames();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
