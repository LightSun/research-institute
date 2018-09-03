package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;

import java.util.List;

/**
 * the left fill info.
 * @author heaven7
 */
public class LeftFillInfo {

    private List<CutInfo.PlaidInfo> plaids;
    private List<MediaPartItem> items;

    public List<CutInfo.PlaidInfo> getPlaids() {
        return plaids;
    }
    public void setPlaids(List<CutInfo.PlaidInfo> plaids) {
        this.plaids = plaids;
    }
    public List<MediaPartItem> getItems() {
        return items;
    }
    public void setItems(List<MediaPartItem> items) {
        this.items = items;
    }
}
