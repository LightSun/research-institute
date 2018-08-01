package com.heaven7.ve.colorgap;

import com.heaven7.ve.VEContext;
import com.heaven7.ve.MediaResourceItem;

/**
 * the image resource scanner
 * @author heaven7
 */
public abstract class ImageResourceScanner{

    public abstract String scan(VEContext context, MediaResourceItem item, String srcDir, String filenamePrefix);

}
