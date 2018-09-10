package com.heaven7.ve.test;

import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.CutItemDelegate;
import com.heaven7.ve.colorgap.MediaPartItem;

import java.util.List;

/**
 * @author heaven7
 */
public interface ShotAssigner {

    int assignShotType(MediaPartItem item);

    List<MediaPartItem> assignShotCuts(ColorGapContext context, CutItemDelegate delegate);

}
