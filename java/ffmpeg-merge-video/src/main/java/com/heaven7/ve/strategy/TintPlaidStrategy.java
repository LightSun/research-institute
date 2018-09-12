package com.heaven7.ve.strategy;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.ve.colorgap.Chapter;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * the strategy of tint plaid
 */
public interface TintPlaidStrategy {
    void tintPlaid(ColorGapContext context, VETemplate template, List<CutInfo.PlaidInfo> plaids, List<MediaPartItem> parts, @Nullable List<Chapter> chapters);
}