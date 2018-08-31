package com.heaven7.ve.colorgap.impl.montage;

import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.Constants;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceScanner;

import java.io.File;

/**
 * @author heaven7
 */
public class LocalHighLightScanner extends MediaResourceScanner {
    @Override
    public String scan(Context context, MediaResourceItem item, String srcDir) {
        // a/b/c.mp4
        // a/b/highlight/c.vhighlight
        String fileName = FileUtils.getFileName(item.getFilePath());
        String fileDir = FileUtils.getFileDir(item.getFilePath(), 1, true);

        return fileDir + File.separator + Constants.DIR_HIGH_LIGHT + File.separator
                + fileName + "."
                + (item.isVideo() ? Constants.EXTENSION_VIDEO_HIGH_LIGHT : Constants.EXTENSION_IMAGE_HIGH_LIGHT);
    }
}
