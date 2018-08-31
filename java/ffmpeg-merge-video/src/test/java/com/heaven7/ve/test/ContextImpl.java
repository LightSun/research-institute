package com.heaven7.ve.test;

import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.collect.PerformanceWriter;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.MusicCutter;
import com.heaven7.ve.kingdom.Kingdom;

public class ContextImpl implements ColorGapContext {

    final ColorGapPerformanceCollector collector = new ColorGapPerformanceCollector(PerformanceWriter.LOG_WRITER);

    @Override
    public void setTestType(int testType) {

    }

    @Override
    public int getTestType() {
        return 0;
    }

    @Override
    public Kingdom getKingdom() {
        return Kingdom.getDefault();
    }
    @Override
    public void setKingdom(Kingdom kingdom) {

    }
    @Override
    public void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector) {

    }
    @Override
    public ColorGapPerformanceCollector getColorGapPerformanceCollector() {
        return collector;
    }

    @Override
    public MusicCutter getMusicCutter() {
        return null;
    }
    @Override
    public void setMusicCutter(MusicCutter provider) {

    }

    @Override
    public void copySystemResource(ColorGapContext dst) {

    }
}