package com.heaven7.ve.colorgap.impl;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.BaseContextOwner;
import com.heaven7.ve.colorgap.FrameFaceRects;

import java.util.ArrayList;
import java.util.List;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.MODULE_CUT_VIDEO;

/**
 * @author heaven7
 */
public class FaceFrameBuffer extends BaseContextOwner{

    public FaceFrameBuffer(Context mContext) {
        super(mContext);
    }

    private static final String TAG = "FrameBuffer";

    private final List<FrameFaceRects> frames = new ArrayList<>();
    private int mainFaceCount;
    private FrameFaceRects pengdingFrame;

    public FrameFaceRects getPengdingFrame() {
        return pengdingFrame;
    }
    public void setPengdingFrame(FrameFaceRects pengdingFrame) {
        this.pengdingFrame = pengdingFrame;
    }

    public boolean isEmpty(){
        return frames.isEmpty();
    }
    public boolean hasPendingFrame(){
        return pengdingFrame != null;
    }
    public void append(FrameFaceRects ft){
        frames.add(ft);
        getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                TAG, "append", "append Face-frame into buffer. frame_idx = " + ft.getFrameIdx());
    }

    public void appendPendingFrame(){
        if(pengdingFrame != null){
            getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                    TAG, "append", "append pending Face-frame into buffer. frame_idx = " + pengdingFrame.getFrameIdx());
            append(pengdingFrame);
            pengdingFrame = null;
        }
    }
    //1. mainFaceCount <= 2, 必须相等
    //2. mainFaceCount >=3, >=3即可
    public boolean isSimilar(int mainFaceCount){
        if(this.mainFaceCount <= 2){
            return this.mainFaceCount == mainFaceCount;
        }else {
            return this.mainFaceCount >= mainFaceCount;
        }
    }
    public void clear(){
        frames.clear();
        pengdingFrame = null;
        getContext().getColorGapPerformanceCollector().addMessage(MODULE_CUT_VIDEO,
                TAG, "clear", "clear buffer");
    }
    public List<FrameFaceRects> getFrames() {
        return frames;
    }

    public int getMainFaceCount() {
        return mainFaceCount;
    }
    public void setMainFaceCount(int mainFaceCount) {
        this.mainFaceCount = mainFaceCount;
    }
}
