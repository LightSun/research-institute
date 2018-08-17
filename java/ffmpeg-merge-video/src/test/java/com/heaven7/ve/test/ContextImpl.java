package com.heaven7.ve.test;

import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.kingdom.Kingdom;

public class ContextImpl implements ColorGapContext {
    @Override
    public Kingdom getKingdom() {
        return Kingdom.getDefault();
    }
    @Override
    public void setKingdom(Kingdom kingdom) {

    }
}