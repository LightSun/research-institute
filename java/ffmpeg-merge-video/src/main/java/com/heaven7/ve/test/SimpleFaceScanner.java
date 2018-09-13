package com.heaven7.ve.test;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceScanner;

import java.io.File;

/**
 * @author heaven7
 */
public class SimpleFaceScanner extends MediaResourceScanner{

    private final String faceDir;

    public SimpleFaceScanner(String faceDir) {
        this.faceDir = faceDir;
    }
    @Override
    public String scan(Context context, BaseMediaResourceItem item, String srcDir) {
        String fileName = FileUtils.getFileName(item.getFilePath());
        return faceDir + File.separator + fileName + "_rects.csv";
    }

}
