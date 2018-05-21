package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Predicates;

import java.util.ArrayList;
import java.util.List;

/**
 * 多人脸信息
 * Created by heaven7 on 2018/4/11 0011.
 */

public class FrameFaceRects {

    private int frameIdx;
    private List<FaceRect> rects;

    public int getFrameIdx() {
        return frameIdx;
    }
    public void setFrameIdx(int frameIdx) {
        this.frameIdx = frameIdx;
    }

    public List<FaceRect> getRects() {
        return rects;
    }
    public void setRects(List<FaceRect> rects) {
        this.rects = rects;
    }

    public boolean hasRect() {
        return !Predicates.isEmpty(rects);
    }

    public void addFaceRect(FaceRect rect) {
        if(rects ==null){
            rects = new ArrayList<>();
        }
        rects.add(rect);
    }
}
