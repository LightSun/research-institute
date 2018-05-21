package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceScanner;
import com.heaven7.ve.colorgap.ResourceInitializer;

/**
 * Created by heaven7 on 2018/4/16 0016.
 */

/*public*/ class MockTagsScanner extends MediaResourceScanner {

    @Override
    public String scan(Context context, MediaResourceItem item, String srcDir) {
        return ResourceInitializer.getFilePathOfTags(item, srcDir);
    }
}
