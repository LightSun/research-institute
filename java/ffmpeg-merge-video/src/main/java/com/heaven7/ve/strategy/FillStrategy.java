package com.heaven7.ve.strategy;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.ve.colorgap.Chapter;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * the strategy of fill plaid
 */
public interface FillStrategy {
    List<GapManager.GapItem> fill(ColorGapContext context, VETemplate template, List<IPlaidInfo> plaids,
                                  List<MediaPartItem> parts, @Nullable List<Chapter> chapters);
}