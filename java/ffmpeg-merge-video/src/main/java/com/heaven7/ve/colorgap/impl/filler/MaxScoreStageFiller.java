package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;

import java.util.List;

/**
 * @author heaven7
 */
public class MaxScoreStageFiller extends StageFiller {
    @Override
    protected void fillImpl(Context context, List<IPlaidInfo> newPlaids, List<MediaPartItem> items, GapManager.GapCallback callback) {
        new GapManager(newPlaids, items).fill(callback, false, true);
    }
}
