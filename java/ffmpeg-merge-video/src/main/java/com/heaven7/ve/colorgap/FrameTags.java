package com.heaven7.ve.colorgap;

import com.heaven7.java.visitor.collection.VisitServices;

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

    public Set<Integer> getTopTagSet(){
        return getTopTagSet(3, 0.5f);
    }
    // 获取概率大于minPossibility的，最大的（3个）tag的id的集合
    public Set<Integer> getTopTagSet(int maxCount, float minPossibility){
        Collections.sort(tags, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return Float.compare(o2.getPossibility(), o1.getPossibility());
            }
        });
        Set<Integer> set = new HashSet<>();
        int count = 0;
        for(Tag tag : tags){
            if(tag.getPossibility() >= minPossibility) {
                set.add(tag.getIndex());
                count ++;
                if(count == maxCount){
                    break;
                }
            }
        }
        return set;
    }
    public List<Tag> getTopTags(int maxCount, float minPossibility){
        Collections.sort(tags, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return Float.compare(o2.getPossibility(), o1.getPossibility());
            }
        });
        List<Tag> tags = new ArrayList<>();
        int count = 0;
        for(Tag tag : tags){
            if(tag.getPossibility() >= minPossibility) {
                tags.add(tag);
                count ++;
                if(count == maxCount){
                    break;
                }
            }
        }
        return tags;
    }
}
