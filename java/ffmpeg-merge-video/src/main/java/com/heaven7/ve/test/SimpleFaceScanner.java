package com.heaven7.ve.test;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceScanner;

import java.io.File;
import java.util.List;

/**
 * @author heaven7
 */
public class SimpleFaceScanner extends SimpleMediaResourceScanner{

    public SimpleFaceScanner(String faceDir) {
        super(faceDir);
    }
    public SimpleFaceScanner(List<String> videoDataDirs, List<String> imageDataDirs) {
        super(videoDataDirs, imageDataDirs);
    }

    @Override
    protected String getImageDataFile(Context context, String imageDataDir, BaseMediaResourceItem item) {
        String fileName = FileUtils.getFileName(item.getFilePath());
        return imageDataDir + File.separator + fileName + "_rects.csv";
    }
    @Override
    protected String getVideoDataFile(Context context, String imageDataDir, BaseMediaResourceItem item) {
        String fileName = FileUtils.getFileName(item.getFilePath());
        return imageDataDir + File.separator + fileName + "_rects.csv";
    }

}
