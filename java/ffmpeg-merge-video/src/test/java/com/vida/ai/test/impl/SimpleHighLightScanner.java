package com.vida.ai.test.impl;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.Constants;
import com.heaven7.ve.colorgap.MediaResourceScanner;

import java.io.File;

/**
 * @author heaven7
 */
public class SimpleHighLightScanner extends MediaResourceScanner {

    private final String dir;

    public SimpleHighLightScanner(String dir) {
        this.dir = dir;
    }

    @Override
    public String scan(Context context, BaseMediaResourceItem item, String srcDir) {
        String fileName = FileUtils.getFileName(item.getFilePath());

        return dir + File.separator + fileName + "." +
                (item.isVideo() ? Constants.EXTENSION_VIDEO_HIGH_LIGHT : Constants.EXTENSION_IMAGE_HIGH_LIGHT);
    }
}
