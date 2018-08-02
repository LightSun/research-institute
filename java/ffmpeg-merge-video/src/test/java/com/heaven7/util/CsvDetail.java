package com.heaven7.util;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.colorgap.FrameTags;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class CsvDetail {
    private static final String[] SUPPORT_FORMATS = {"mp4","jpg","jpeg","png"};

    private MediaSource mediaSrc;
    private String tagPath;
    private List<FrameTags> frames;

    public void setMediaSourceFromTagPath(String tagPath){
        this.tagPath = tagPath;

        String name = FileUtils.getFileName(tagPath);
        int index = name.lastIndexOf("_");
        Throwables.checkNegativeValue(index);
        String filename = name.substring(0, index);

        String fileDir = FileUtils.getFileDir(tagPath, 2, true);
        //check mp4.
        populateMediaSource(fileDir, filename);
    }

    private void populateMediaSource(String fileDir, String filename) {
        for (String format : SUPPORT_FORMATS){
            MediaSource ms = getMediaSource0(fileDir, filename, format);
            if(ms != null){
                mediaSrc = ms;
                return;
            }
        }
        throw new IllegalStateException("can't find media source");
    }

    private static MediaSource getMediaSource0(String fileDir, String filename, String extensition){
        File file = new File(fileDir, filename + "." + extensition);
        if(file.exists()){
            final int type;
            final boolean video;
            if(extensition.equalsIgnoreCase("mp4")){
                type = MediaSource.TYPE_MP4;
                video = true;
            }else if(extensition.equalsIgnoreCase("jpg")){
                type = MediaSource.TYPE_JPG;
                video = false;
            }else if(extensition.equalsIgnoreCase("jpeg")){
                type = MediaSource.TYPE_JPEG;
                video = false;
            }else if(extensition.equalsIgnoreCase("png")){
                type = MediaSource.TYPE_PNG;
                video = false;
            }else{
                throw new UnsupportedOperationException();
            }
            MediaSource ms = new MediaSource();

            ms.setType(type);
            ms.setVideo(video);
            ms.setFilePath(file.getAbsolutePath());
            return ms;
        }
        //if all is lower
        if( Pattern.compile("[a-z]+").matcher(extensition).matches()) {
            return getMediaSource0(fileDir, filename, extensition.toUpperCase());
        }
        return null;
    }

    public String getFilePath(){
        return getMediaSrc().getFilePath();
    }

    public MediaSource getMediaSrc() {
        return mediaSrc;
    }
    public void setMediaSrc(MediaSource mediaSrc) {
        this.mediaSrc = mediaSrc;
    }

    public String getTagPath() {
        return tagPath;
    }
    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public List<FrameTags> getFrames() {
        return frames;
    }
    public void setFrames(List<FrameTags> frames) {
        this.frames = frames;
    }

}
