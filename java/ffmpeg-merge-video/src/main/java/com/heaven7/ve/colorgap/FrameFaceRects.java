package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 多人脸信息
 * Created by heaven7 on 2018/4/11 0011.
 */

public class FrameFaceRects {

    private int frameIdx;
    private List<FaceRect> rects;

    //temp
    private List<Float> sortedFrameAreas;

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
        if(rects == null){
            rects = new ArrayList<>();
        }
        rects.add(rect);
    }

    public List<Float> getSortedFrameAreas() {
        if(!hasRect()){
            return Collections.emptyList();
        }
        if(sortedFrameAreas == null){
            sortedFrameAreas = new ArrayList<>();
            //面积降序
            VisitServices.from(getRects()).transformToCollection(null,
                    new Comparator<Float>() {
                        @Override
                        public int compare(Float o1, Float o2) {
                            return Float.compare(o2, o1);
                        }
                    },
                    (faceRect, param) -> faceRect.getWidth() * faceRect.getHeight()
            ).save(sortedFrameAreas);
        }
        return sortedFrameAreas;
    }

    public int getMainFaceCount() {
        if(!hasRect()){
            return 0;
        }
        return VEGapUtils.getMainFaceCount(getSortedFrameAreas());
    }

    public int getRectsCount() {
        return rects != null ? rects.size() : 0;
    }
}
