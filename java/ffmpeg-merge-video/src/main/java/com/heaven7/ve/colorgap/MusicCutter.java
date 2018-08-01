package com.heaven7.ve.colorgap;


import com.heaven7.ve.VEContext;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public interface MusicCutter {
    CutInfo[] cut(VEContext context, String[] musicPath);
}
