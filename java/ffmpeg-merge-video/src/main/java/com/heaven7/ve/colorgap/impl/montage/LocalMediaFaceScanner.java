package com.heaven7.ve.colorgap.impl.montage;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceScanner;

import java.io.File;

/**
 * @author heaven7
 */
public class LocalMediaFaceScanner extends MediaResourceScanner {

    @Override
    public String scan(Context context, MediaResourceItem item, String srcDir) {
        // a/b/c.mp4
        // a/b/c/c_rects.csv
        String fileName = FileUtils.getFileName(item.getFilePath());
        String fileDir = FileUtils.getFileDir(item.getFilePath(), 1, true);
        return fileDir + File.separator + fileName + File.separator +  fileName + "_rects.csv";
    }
}
