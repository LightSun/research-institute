package com.heaven7.ve.colorgap;

import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * the shader of story-line/
 * Created by heaven7 on 2018/5/12 0012.
 */

public interface StoryLineShader {

    /** help debug */
    boolean DEBUG = true;
    /**
     * tint the plaids by story-line.
     * @param plaids the all plaids of selected music.
     * @param template the template which from base tint.
     * @param items the cutted items.
     * @param filler the plaid filler
     * @param filter the air shot filter
     * @return the gap items.
     */
    List<GapManager.GapItem> tintAndFill(ColorGapContext context, List<CutInfo.PlaidInfo> plaids, VETemplate template, List<MediaPartItem> items,
                                         PlaidFiller filler, AirShotFilter filter);

}
