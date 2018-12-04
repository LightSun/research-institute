package com.heaven7.audiomix.sample.mix;

import java.io.File;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
public interface MediaMixContext{

    int UNSUPPORT = -1;
    int TYPE_MP3 = 1;
    int TYPE_AAC = 2;
    int TYPE_M4A = 3;
    int TYPE_MP4 = 10;

    /**
     * get the media type from target file.
     * @return the media type. {@linkplain #UNSUPPORT} means the file is not support
     */
    default int getMediaType(String path){
        if(path != null){
            File file = new File(path);
            if(file.exists() && file.isFile()){
                if(path.endsWith(".mp3")){
                    return TYPE_MP3;
                } else if(path.endsWith(".aac")){
                    return TYPE_AAC;
                }
                else if(path.endsWith(".m4a")){
                    return TYPE_M4A;
                }
                else if(path.endsWith(".mp4")){
                    return TYPE_MP4;
                }
            }
        }
        return UNSUPPORT;
    }
}
