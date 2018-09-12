package com.heaven7.ve.strategy;

import com.heaven7.ve.colorgap.Chapter;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

public interface GroupStrategy {

    List<Chapter> groupChapter(ColorGapContext context, VETemplate template, List<MediaPartItem> parts);
}