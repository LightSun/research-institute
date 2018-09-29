package com.heaven7.ve.colorgap.impl.montage;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;

import com.heaven7.ve.colorgap.MediaResourceScanner;
import com.heaven7.ve.cross_os.IMediaResourceItem;

import java.io.File;

/**
 * @author heaven7
 */
public class LocalTagScanner extends MediaResourceScanner {

    private static final String TAG = "MediaTagScanner";

    @Override
    public String scan(Context context, IMediaResourceItem item, String srcDir) {
        // a/b/c.mp4
        // a/b/c/c_predictions.csv
        String fileName = FileUtils.getFileName(item.getFilePath());
        String parentDir = FileUtils.getFileDir(item.getFilePath(), 1, true);
        return parentDir + File.separator + fileName + File.separator
                + fileName + "_predictions.csv";
    }

}
