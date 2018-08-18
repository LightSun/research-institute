package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class SimpleColorGapContext implements ColorGapContext {

    private Kingdom mKingdom;
    private ColorGapPerformanceCollector mCollector;

    @Override
    public Kingdom getKingdom() {
        return mKingdom;
    }
    @Override
    public void setKingdom(Kingdom kingdom) {
        this.mKingdom = kingdom;
    }

    @Override
    public void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector) {
        this.mCollector = collector;
    }
    @Override
    public ColorGapPerformanceCollector getColorGapPerformanceCollector() {
        return mCollector;
    }
}
