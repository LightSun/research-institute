package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.kingdom.ModuleData;
import com.heaven7.ve.template.EffectData;

import java.util.List;

/**
 * @author heaven7
 */
public class EffectMarkerImpl implements EffectsMarker {

    @Override
    public void markEffects(ColorGapContext context, List<Chapter> chapters, List<GapManager.GapItem> gapItems) {
        EffectData effectData = context.getFileResourceManager().getEffectData();
        //texture
        EffectData.Data textureData = effectData.getTextureData();
        if(textureData != null && textureData.isValid()){
            mark(MarkFlags.TYPE_TEXTURE ,textureData, chapters, gapItems);
        }
        //transition
        EffectData.Data transitionData = effectData.getTransitionData();
        if(transitionData != null && transitionData.isValid()){
            mark(MarkFlags.TYPE_TRANSITION, transitionData, chapters, gapItems);
        }
        //filter
        EffectData.Data filterData = effectData.getFilterData();
        if(filterData != null && filterData.isValid()){
            mark(MarkFlags.TYPE_FILTER, filterData, chapters, gapItems);
        }
        //special effect
        EffectData.Data specialEffectData = effectData.getSpecialEffectData();
        if(specialEffectData != null && specialEffectData.isValid()){
            mark(MarkFlags.TYPE_SPECIAL_EFFECT, specialEffectData, chapters, gapItems);
        }

        //after mark. applyEffects
        VisitServices.from(gapItems).map(new ResultVisitor<GapManager.GapItem, MediaPartItem>() {
            @Override
            public MediaPartItem visit(GapManager.GapItem gapItem, Object param) {
                return (MediaPartItem) gapItem.item;
            }
        }).asListService().fireMulti(2, 1, null, new FireMultiVisitor<MediaPartItem>() {
            @Override
            public void visit(Object param, int count, int step, List<MediaPartItem> parts) {
                MediaPartItem leftItem = parts.get(0);
                MediaPartItem rightItem = parts.get(1);
                leftItem.applyEffects(rightItem.videoPart);
            }
        });
    }
    private static void mark(int markType, EffectData.Data data, List<Chapter> chapters, List<GapManager.GapItem> gapItems) {
        final int itemSize = gapItems.size();
        //index
        List<EffectData.Item> items = data.getIndexItems();
        if(!Predicates.isEmpty(items)){
            VisitServices.from(items).fire(new FireVisitor<EffectData.Item>() {
                @Override
                public Boolean visit(EffectData.Item item, Object param) {
                    int index = getRealIndex(item.getIndex(), itemSize);
                    if(index < 0 ||index >= itemSize){
                        return false;
                    }
                    MediaPartItem mpi = (MediaPartItem) gapItems.get(index).item;
                    mpi.getMarkFlags().addEffectItemDelegate(markType, new MarkFlags.EffectItemDelegate(item, MarkFlags.FLAG_INDEX));
                    return false;
                }
            });
        }
        //chapter
        items = data.getChapterItems();
        if(!Predicates.isEmpty(items)){
            VisitServices.from(items).fire(new FireVisitor<EffectData.Item>() {
                @Override
                public Boolean visit(EffectData.Item item, Object param) {
                    VisitServices.from(chapters).fireWithStartEnd(new StartEndVisitor<Chapter>() {
                        @Override
                        public boolean visit(Object param, Chapter chapter, boolean start, boolean end) {
                            int index = getRealIndex(item.getIndex(),  chapter.getPlaidCount());
                            if(index < 0 || index >= chapter.getPlaidCount()){
                                return false;
                            }
                            MediaPartItem mpi = (MediaPartItem) chapter.getFilledItems().get(index).item;
                            if(!mpi.getMarkFlags().hasFlags(markType, MarkFlags.FLAG_INDEX)) {
                                mpi.getMarkFlags().addEffectItemDelegate(markType,
                                        new MarkFlags.EffectItemDelegate(item, MarkFlags.FLAG_CHAPTER));
                            }
                            return false;
                        }
                    });
                    return false;
                }
            });
        }
        //level
        items = data.getLevelItems();
        if(!Predicates.isEmpty(items)){
            List<MediaPartItem> parts = VisitServices.from(gapItems).map(
                    new ResultVisitor<GapManager.GapItem, MediaPartItem>() {
                @Override
                public MediaPartItem visit(GapManager.GapItem gapItem, Object param) {
                    return (MediaPartItem) gapItem.item;
                }
            }).getAsList();
            VisitServices.from(items).fire(new FireVisitor<EffectData.Item>() {
                @Override
                public Boolean visit(EffectData.Item item, Object param) {
                    VisitServices.from(parts).filter(new PredicateVisitor<MediaPartItem>() {
                        @Override
                        public Boolean visit(MediaPartItem mpi, Object param) {
                            ModuleData moduleData = mpi.getHighLightModuleData();
                            return moduleData != null && moduleData.getLevel() == item.getLevel();
                        }
                    }).fire(new FireVisitor<MediaPartItem>() {
                        @Override
                        public Boolean visit(MediaPartItem mpi, Object param) {
                            MarkFlags markFlags = mpi.getMarkFlags();
                            if(!markFlags.hasFlags(markType, MarkFlags.FLAG_INDEX)
                                    && !markFlags.hasFlags(markType, MarkFlags.FLAG_CHAPTER)){
                                markFlags.addEffectItemDelegate(markType, new MarkFlags.EffectItemDelegate(item, MarkFlags.FLAG_LEVEL));
                            }
                            return null;
                        }
                    });
                    return false;
                }
            });
        }
    }

    private static int getRealIndex(int index, int itemSize){
        if(index >= 0){
            return index;
        }
        //倒数
        return itemSize - Math.abs(index);
    }
}
