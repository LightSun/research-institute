package com.heaven7.ve.colorgap;

import com.heaven7.ve.gap.GapManager;

import java.util.List;

/**
 * @author heaven7
 */
public interface EffectsMarker {

    /**
     * mark effects for gap-items.
     * @param context the context
     * @param chapter the chapters
     * @param gapItems the gap items.
     */
    void markEffects(ColorGapContext context, List<Chapter> chapter, List<GapManager.GapItem> gapItems);
}
