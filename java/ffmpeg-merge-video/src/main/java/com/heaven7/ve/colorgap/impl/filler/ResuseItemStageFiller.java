package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.gap.GapManager;

import java.util.List;

/**
 * @author heaven7
 */
public class ResuseItemStageFiller extends MatchStageFiller{

    @Override
    public void fillImpl(Context context, List<CutInfo.PlaidInfo> plaids, List<MediaPartItem> items, GapManager.GapCallback callback) {
        new GapManager(plaids, items).fill(callback, true, false);
    }
}
