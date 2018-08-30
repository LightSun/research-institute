package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class SimpleColorGapContext implements ColorGapContext {

    private int mType = TEST_TYPE_SERVER;
    private Kingdom mKingdom;
    private ColorGapPerformanceCollector mCollector;

    @Override
    public void setTestType(int testType) {
        this.mType = testType;
    }
    @Override
    public int getTestType() {
        return mType;
    }

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
