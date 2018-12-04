package com.heaven7.audiomix.sample.utils;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
public class MediaUtils {

    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_VIDEO = 2;

    @TargetApi(18)
    public static MediaFormat getMediaFormat(MediaExtractor extractor, int type, String path) throws IOException {
        int trackIndex = -1;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            extractor.setDataSource(fis.getFD());
            if (type == TYPE_VIDEO) {
                for (int i = 0, size = extractor.getTrackCount(); i < size; i++) {
                    MediaFormat trackFormat = extractor.getTrackFormat(i);
                    String mime = trackFormat.getString(MediaFormat.KEY_MIME);
                    if (mime.startsWith("video")) {
                        trackIndex = i ;
                        break;
                    }
                }
            } else if (type == TYPE_AUDIO) {
                for (int i = 0, size = extractor.getTrackCount(); i < size; i++) {
                    MediaFormat trackFormat = extractor.getTrackFormat(i);
                    String mime = trackFormat.getString(MediaFormat.KEY_MIME);
                    if (mime.startsWith("audio")) {
                        trackIndex = i ;
                        break;
                    }
                }
            } else {
                throw new UnsupportedOperationException("wrong type = " + type);
            }
            extractor.selectTrack(trackIndex);
            extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            return extractor.getTrackFormat(trackIndex);
        }finally {
            if(fis != null){
                fis.close();
            }
        }
    }
}
