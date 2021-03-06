package com.heaven7.ve.colorgap;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.kingdom.Kingdom;

import java.util.*;

/**
 * 单帧图像的tag信息
 * Created by heaven7 on 2018/4/11 0011.
 */

public class FrameTags {

    private int frameIdx;
    private final List<Tag> tags = new ArrayList<>();

    public int getFrameIdx() {
        return frameIdx;
    }
    public void setFrameIdx(int frameIdx) {
        this.frameIdx = frameIdx;
    }

    public List<Tag> getTags() {
        return tags;
    }
    public void addTag(Tag tag){
        VisitServices.from(tags).addIfNotExist(tag);
    }

    public Set<Integer> getTopTagSet(ColorGapContext context){
        return getTopTagSet(context, 3, 0.5f);
    }

    // 获取概率大于minPossibility的，最大的（3个）tag的id的集合
    public Set<Integer> getTopTagSet(ColorGapContext context,int maxCount, float minPossibility){
        List<Tag> topTags = getTopTags(context, maxCount, minPossibility, Kingdom.TYPE_ALL);
        HashSet<Integer> set = new HashSet<>();
        VisitServices.from(topTags).map(new ResultVisitor<Tag, Integer>() {
            @Override
            public Integer visit(Tag tag, Object param) {
                return tag.getIndex();
            }
        }).save(set);
        return set;
    }

    /**
     * get top tags.
     *
     * @param context
     * @param maxCount max count
     * @param minPossibility the min possibility
     * @param vocabularyType the vocabulary type. see {@linkplain Kingdom#TYPE_ALL} and etc.
     * @return the top tags.
     */
    public List<Tag> getTopTags(ColorGapContext context, int maxCount, float minPossibility, int vocabularyType){

        final List<Tag> result = new ArrayList<>();
        final List<Tag> filterTags;

        switch (vocabularyType){
            case Kingdom.TYPE_NOUN:
            case Kingdom.TYPE_ADJECTIVE:
            case Kingdom.TYPE_SCOPE:
                filterTags = VisitServices.from(this.tags).visitForQueryList(new PredicateVisitor<Tag>() {
                    @Override
                    public Boolean visit(Tag tag, Object param) {
                        return context.getKingdom().isTagExists(vocabularyType, tag.getIndex());
                    }
                }, null);
                break;

            case Kingdom.TYPE_ALL:
                filterTags = this.tags;
                break;

            default:
                throw new UnsupportedOperationException("un-support vocabulary type = "+ vocabularyType);
        }
        //降序
        Collections.sort(filterTags, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return Float.compare(o2.getPossibility(), o1.getPossibility());
            }
        });
        int count = 0;
        for(Tag tag : filterTags){
            if(tag.getPossibility() >= minPossibility) {
                result.add(tag);
                count ++;
                if(count == maxCount){
                    break;
                }
            }
        }
        return result;
    }
}
