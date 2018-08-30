package com.heaven7.ve.colorgap.impl;

import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.BaseContextOwner;
import com.heaven7.ve.colorgap.FrameTags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.MODULE_CUT_VIDEO;

/**
 * @author heaven7
 */
public class FrameBuffer extends BaseContextOwner{

    private static final String TAG = "FrameBuffer";

    private final List<FrameTags> frames = new ArrayList<>();
    private Set<Integer> tagSet = new HashSet<>();
    private FrameTags pengdingFrame;

    public FrameBuffer(Context mContext) {
        super(mContext);
    }

    public FrameTags getPengdingFrame() {
        return pengdingFrame;
    }
    public void setPengdingFrame(FrameTags pengdingFrame) {
        this.pengdingFrame = pengdingFrame;
    }

    public boolean isEmpty(){
        return frames.isEmpty();
    }
    public boolean hasPendingFrame(){
        return pengdingFrame != null;
    }
    public void append(FrameTags ft){
        append(ft, true);
    }
    public void append(FrameTags ft, boolean updateTagSet){
        Set<Integer> topTagSet = ft.getTopTagSet(getContext());
        if(topTagSet.isEmpty()){
            return;
        }
        frames.add(ft);
        if(updateTagSet){
            if(tagSet.isEmpty()){
                tagSet.addAll(topTagSet);
            }else{
                tagSet = CollectionUtils.intersection(tagSet, topTagSet);
            }
        }
        getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                TAG, "append", "append frame into buffer. frame_idx = " + ft.getFrameIdx());
    }

    public void appendPendingFrame(){
        if(pengdingFrame != null){
            getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                    TAG, "append", "append pending frame into buffer. frame_idx = " + pengdingFrame.getFrameIdx());
            append(pengdingFrame, false);
            pengdingFrame = null;
        }
    }

    /** get similar score. if score > 0.3f. means similar */
    public float getSimilarScore(FrameTags ft){
        Set<Integer> topTagSet = ft.getTopTagSet(getContext());
        float score = 0f;
        if(!topTagSet.isEmpty()){
            Set<Integer> newSet = CollectionUtils.intersection(tagSet, topTagSet);
            score = newSet.size() * 1f / tagSet.size();
        }
        return score;
    }

    public void clear(){
        frames.clear();
        tagSet = new HashSet<>();
        pengdingFrame = null;
        getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                TAG, "clear", "clear buffer");
    }

    public static boolean isSimilar(float score){
        return score > 0.3f;
    }

    public List<FrameTags> getFrames() {
        return frames;
    }

    public Set<Integer> getTagSet() {
        return tagSet;
    }
}
