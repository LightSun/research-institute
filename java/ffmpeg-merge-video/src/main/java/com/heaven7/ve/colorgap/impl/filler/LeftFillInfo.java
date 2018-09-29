package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;

import java.util.List;

/**
 * the left fill info.
 * @author heaven7
 */
public class LeftFillInfo {

    private List<IPlaidInfo> plaids;
    private List<MediaPartItem> items;

    public List<IPlaidInfo> getPlaids() {
        return plaids;
    }
    public void setPlaids(List<IPlaidInfo> plaids) {
        this.plaids = plaids;
    }
    public List<MediaPartItem> getItems() {
        return items;
    }
    public void setItems(List<MediaPartItem> items) {
        this.items = items;
    }
}
