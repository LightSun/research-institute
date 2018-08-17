package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class SimpleColorGapContext implements ColorGapContext {

    private Kingdom mKingdom;

    @Override
    public Kingdom getKingdom() {
        return mKingdom;
    }
    @Override
    public void setKingdom(Kingdom kingdom) {
        this.mKingdom = kingdom;
    }
}
