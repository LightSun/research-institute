package com.heaven7.ve.colorgap;

import java.util.List;

/**
 * the item of frame buffer.
 *
 * @author heaven7
 */
public class FrameItem {

    public final int id;
    /**
     * like face areas.
     */
    public final List<Float> areas;

    public FrameItem(int id, List<Float> areas) {
        this.id = id;
        this.areas = areas;
    }
}
