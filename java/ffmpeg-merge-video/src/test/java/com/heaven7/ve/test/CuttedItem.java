package com.heaven7.ve.test;

import com.heaven7.ve.gap.GapManager;

/**
 * the cutted item
 */
public class CuttedItem {

    private GapManager.GapItem item;
    private String savePath;

    public GapManager.GapItem getItem() {
        return item;
    }
    public void setItem(GapManager.GapItem item) {
        this.item = item;
    }

    public String getSavePath() {
        return savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

}
