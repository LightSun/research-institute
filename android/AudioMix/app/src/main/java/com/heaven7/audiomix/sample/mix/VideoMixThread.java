package com.heaven7.audiomix.sample.mix;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Process;

import com.heaven7.audiomix.sample.utils.MediaUtils;

import java.io.IOException;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
@TargetApi(18)
/*public*/ class VideoMixThread extends MediaMixThread {

    private int mTrackIndex = -1;

    public VideoMixThread(String path) {
        super(path);
    }

    @Override
    protected void initImpl(MediaMuxer muxer) throws IOException {
        MediaFormat format = MediaUtils.getMediaFormat(getMediaExtractor(), MediaUtils.TYPE_VIDEO, getPath());
        mTrackIndex = muxer.addTrack(format);
        getMediaInfo(format);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        getMediaMixManageDelegate().markVideoStart();
        readAndWriteDirectly(mTrackIndex);
        getMediaMixManageDelegate().markVideoEnd();
    }
}
