package com.heaven7.ve.test;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;

import com.heaven7.ve.Constants;
import com.heaven7.ve.cross_os.IMediaResourceItem;

import java.io.File;
import java.util.List;

/**
 * @author heaven7
 */
public class SimpleHighLightScanner extends SimpleMediaResourceScanner {

    public SimpleHighLightScanner(String dataDir) {
        super(dataDir);
    }
    public SimpleHighLightScanner(List<String> videoDataDirs, List<String> imageDataDirs) {
        super(videoDataDirs, imageDataDirs);
    }

    @Override
    protected String getImageDataFile(Context context, String imageDataDir, IMediaResourceItem item) {
        String fileName = FileUtils.getFileName(item.getFilePath());
        return imageDataDir + File.separator + fileName +  "." + Constants.EXTENSION_IMAGE_HIGH_LIGHT;
    }
    @Override
    protected String getVideoDataFile(Context context, String imageDataDir, IMediaResourceItem item) {
        String fileName = FileUtils.getFileName(item.getFilePath());
        return imageDataDir + File.separator + fileName +  "." + Constants.EXTENSION_VIDEO_HIGH_LIGHT;
    }
}
