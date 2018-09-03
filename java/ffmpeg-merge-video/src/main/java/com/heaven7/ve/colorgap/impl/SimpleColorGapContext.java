package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.anno.SystemResource;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.MusicCutter;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class SimpleColorGapContext implements ColorGapContext {

    private Kingdom mKingdom;
    private ColorGapPerformanceCollector mCollector;
    @SystemResource
    private MusicCutter mMusicCutter;
    @SystemResource
    private InitializeParam mInitParam;

    @Override
    public void setInitializeParam(InitializeParam ip) {
        this.mInitParam = ip;
    }
    @Override
    public InitializeParam getInitializeParam() {
        return mInitParam;
    }
    @Override
    public int getTestType() {
        return mInitParam != null ? mInitParam.getTestType() : TEST_TYPE_SERVER;
    }

    @Override
    public Kingdom getKingdom() {
        return mKingdom;
    }
    @Override
    public void setKingdom(Kingdom kingdom) {
        this.mKingdom = kingdom;
        //resolve the associate config file. like template
        kingdom.resolveFileResources(this);
    }

    @Override
    public void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector) {
        this.mCollector = collector;
    }
    @Override
    public ColorGapPerformanceCollector getColorGapPerformanceCollector() {
        return mCollector;
    }

    @Override
    public MusicCutter getMusicCutter() {
        return mMusicCutter;
    }
    @Override
    public void setMusicCutter(MusicCutter provider) {
        this.mMusicCutter = provider;
    }

    @Override
    public void copySystemResource(ColorGapContext dst) {
        dst.setMusicCutter(getMusicCutter());
        dst.setInitializeParam(getInitializeParam());
    }
}
