package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public interface ColorGapContext extends Context {

    Kingdom getKingdom();
    void setKingdom(Kingdom kingdom);

    void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector);
    ColorGapPerformanceCollector getColorGapPerformanceCollector();
}
