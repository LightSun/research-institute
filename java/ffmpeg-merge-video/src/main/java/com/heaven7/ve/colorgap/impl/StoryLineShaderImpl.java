package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.Collections2;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by heaven7 on 2018/5/12 0012.
 */

public class StoryLineShaderImpl implements StoryLineShader {

    private static final String TAG = "StoryLineShaderImpl";
    private static final Comparator<MediaPartItem> TIME_COMPARATOR
            = (Comparator<MediaPartItem>) (o1, o2) -> Long.compare(o1.getStartTime(), o2.getStartTime());

    @Override
    public List<GapManager.GapItem> tintAndFill(List<CutInfo.PlaidInfo> plaids, VETemplate template, List<MediaPartItem> items,
                                                PlaidFiller filler, AirShotFilter filter) {
        /*
           0, 把空镜头过滤出来。 最后再填充空镜头， 根据时间或者tag?
         * 1, 按照章节。把items划分到对应章节。
         * 2，如果章节没有item.或者不够时， 直接填充高分的镜头
         * 3, 章节内部按照故事线来填. (章节优先级？)
         */
        if(DEBUG) {
            Logger.d(TAG, "tintAndFill", ">>> start story shader...");
        }
        List<MediaPartItem> emptyItems = getEmptyShots(items);
        if(DEBUG) {
            Logger.d(TAG, "tintAndFill", "get air shots: " + emptyItems);
        }
        items.removeAll(emptyItems);
        List<Chapter> chapters = groupChapter(template, items);
        //normal fill
        for (Chapter chapter : chapters){
            chapter.fill(emptyItems, filler, filter);
        }
        //process air-shot between chapters
        for(int i = 0, size = chapters.size() ; i < size ; i ++){
            Chapter chapter = chapters.get(i);
            if(chapter.isAir()){
                CutInfo.PlaidInfo airPlaid = chapter.getAirPlaid();
                try {
                    MediaPartItem item = filter.filter(airPlaid, chapters.get(i - 1), chapters.get(i + 1), emptyItems);
                    if(item != null) {
                        GapManager.GapItem gapItem = new GapManager.GapItem(airPlaid, item);
                        gapItem.markHold();
                        chapter.addFilledItem(gapItem);
                    }
                }catch (IndexOutOfBoundsException e){
                    throw new RuntimeException("script of air shot is error", e);
                }
            }
        }

        //build result
        List<GapManager.GapItem> result = new ArrayList<>();
        for(Chapter chapter : chapters){
            result.addAll(chapter.getFilledItems());
        }
        if(DEBUG){
            onPostFill(chapters);
        }
        return result;
    }

    protected void onPostFill(List<Chapter> chapters) {

    }

    private List<Chapter> groupChapter(VETemplate template, List<MediaPartItem> items) {
        List<Chapter> chapters = new ArrayList<>();
        List<VETemplate.LogicSentence> sentences = template.getLogicSentences();
        //only one dir (often for c user.)
        if(sentences.size() == 1){
            //only one
            items.sort(TIME_COMPARATOR);
            chapters.add(new Chapter(sentences.get(0).getPlaids(), items, 0));
            return chapters;
        }

        for(int i = 0 , size = sentences.size() ; i < size ; i ++){
            VETemplate.LogicSentence ls = sentences.get(i);
            List<MediaPartItem> filterItems = VisitServices.from(items)
                    .visitForQueryList(
                    new PredicateVisitor<MediaPartItem>() {
                @Override
                public Boolean visit(MediaPartItem partItem, Object param) {
                    String dir = VEGapUtils.getFileDir(partItem.item.getFilePath(), 1, false);
                    return dir != null && dir.equals(ls.getDir());
                }
            }, null);
            filterItems.sort(TIME_COMPARATOR);
            if(filterItems.isEmpty()){
                throw new IllegalStateException("RawScript error. dir = " + ls.getDir());
            }
            chapters.add(new Chapter(ls.getPlaids(), filterItems, i));
        }
        return chapters;
    }

    private  List<MediaPartItem> getEmptyShots(List<MediaPartItem> items) {

        return VisitServices.from(items).visitForQueryList(new PredicateVisitor<MediaPartItem>() {
            @Override
            public Boolean visit(MediaPartItem partItem, Object param) {
                String dir = VEGapUtils.getFileDir(partItem.item.getFilePath(), 1,  false);
                return dir != null && MetaInfo.DIR_EMPTY.equals(dir);
            }
        }, null);
    }



}
