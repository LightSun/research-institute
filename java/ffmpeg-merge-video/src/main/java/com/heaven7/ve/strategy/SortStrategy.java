package com.heaven7.ve.strategy;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.ve.colorgap.Chapter;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

public interface SortStrategy {
    List<GapManager.GapItem> sort(ColorGapContext context, VETemplate template, List<IPlaidInfo> plaids,
                                  List<MediaPartItem> parts, @Nullable List<Chapter> chapter, List<GapManager.GapItem> gapItems);
}