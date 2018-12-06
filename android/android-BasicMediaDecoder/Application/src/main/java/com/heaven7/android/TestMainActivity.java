package com.heaven7.android;

import com.example.android.basicmediadecoder.MainActivity;
import com.heaven7.android.sample.TestCameraToMpegActivity;
import com.heaven7.android.sample.TestEncodeAndMuxActivity;
import com.heaven7.android.sample.TestExtractMpegFramesActivity;
import com.heaven7.android.sample.TestMediaCodecAudioTrackActivity;

import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(MainActivity.class));
        list.add(new ActivityInfo(TestMediaCodecAudioTrackActivity.class));
        list.add(new ActivityInfo(TestEncodeAndMuxActivity.class));
        list.add(new ActivityInfo(TestCameraToMpegActivity.class));
        list.add(new ActivityInfo(TestExtractMpegFramesActivity.class));
    }

}
