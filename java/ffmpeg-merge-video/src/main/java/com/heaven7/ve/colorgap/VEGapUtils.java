package com.heaven7.ve.colorgap;


import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.gap.ItemDelegate;
import com.heaven7.ve.gap.PlaidDelegate;
import com.heaven7.ve.kingdom.Kingdom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.heaven7.utils.CommonUtils.isInRange;

/**
 * Created by heaven7 on 2018/5/15 0015.
 */

public class VEGapUtils {

    // private static final String TAG = "VEGapUtils";
    private static final float MAIN_FACE_AREA_RATE          = 2.0f ;        // 主人脸相对次要人脸的面积倍率
    private static final float AVERAGE_AREA_DIFF_RATE       = 0.5f  ;       // 多人脸场景中，次要人脸相对平均人脸面积的倍率


    public static void adjustTime(ColorGapContext context,List<GapManager.GapItem> filledItems){
        Kingdom kingdom = context.getKingdom();
        for(GapManager.GapItem gapItem : filledItems) {
            CutInfo.PlaidInfo plaid = (CutInfo.PlaidInfo) gapItem.plaid;
            MediaPartItem mpi = (MediaPartItem) gapItem.item;
            //for image. set time directly
            if(mpi.item.isImage()){
                mpi.videoPart.setStartTime(0);
                mpi.videoPart.setEndTime(plaid.getDuration());
                continue;
            }
            TimeTraveller videoPart = mpi.videoPart;
            if(videoPart.getMaxDuration() < plaid.getDuration()){
                throw new IllegalStateException("caused by video max duration < part music duration.");
            }
            //if duration is the same . no need adjust time.
            if(videoPart.getDuration() != plaid.getDuration()){
                if(videoPart.getMaxDuration() < plaid.getDuration()){
                    throw new IllegalStateException("video relative music part is too short.");
                }
                if(kingdom.isGeLaiLiYa()){
                    videoPart.adjustTimeAsCenter(plaid.getDuration());
                }else {
                    //the key frame time often is the high-light time
                    int keyFrameTime = mpi.getKeyFrameTime();
                    videoPart.adjustTime(CommonUtils.timeToFrame(keyFrameTime, TimeUnit.SECONDS),
                            plaid.getDuration());
                }
            }
        }
    }

    public static Kingdom getKingdom(Object context){
        if(context instanceof ColorGapContext){
            return ((ColorGapContext) context).getKingdom();
        }
        throw new IllegalStateException("context must instance of ");
    }
    public static ColorGapContext asColorGapContext(Object context){
        if(context instanceof ColorGapContext){
           return (ColorGapContext) context;
        }
        throw new IllegalStateException("context must instance of ");
    }

    public static <Item extends ItemDelegate> Item filterByScore(PlaidDelegate plaid, List<Item> items){
        //check hold until find , if not found , not check hold
        Item item = filterByScore0(plaid, items, true);
        if(item != null){
            return item;
        }
        item = filterByScore0(plaid, items, false);
        return item;
    }
    private static <Item extends ItemDelegate> Item filterByScore0(PlaidDelegate plaid, List<Item> items, boolean checkHold){
        float maxScore = -1f;
        Item bestItem = null;
        for(Item item : items){
            if(checkHold && item.isHold()){
                continue;
            }
            // int score = filter.computeScoreWithWeight(mpi.getColorCondition());
            float filterScore = plaid.computeScore(item.getColorCondition());
            float tagScore = item.getDomainTagScore();
            float score = filterScore + tagScore;
            if(score > maxScore){
                maxScore = score;
                bestItem = item;
            }
        }
        return bestItem;
    }

    /** 根据“主人脸”面积获取镜头类型 */
    public static String getShotType(float mainFaceArea){
      /*  let closeUp         = 0.12..<1.0    // 特写（头）
        let mediaCloseUp    = 0.08..<0.12   // 特写（胸）
        let mediumShot      = 0.050..<0.08  // 中景（腰）
        let mediumLongShot   = 0.020..<0.050 // 中远景（膝）
        let longShot        = 0.003..<0.020 // 远景（全身）
        let veryLongShot    = 0.001..<0.003 // 大远景*/

        if (isInRange(mainFaceArea,0.12f, 1.0f)) {
            return "closeUp";
        } else if (isInRange(mainFaceArea,0.06f, 0.12f)) {
            return "mediaCloseUp";
        } else if (isInRange(mainFaceArea,0.04f, 0.06f)) {
            return "mediumShot";
        } else if (isInRange(mainFaceArea,0.02f, 0.04f)) {
            return "mediumLongShot";
        } else if (isInRange(mainFaceArea,0.003f, 0.02f)) {
            return "longShot";
        } else if (isInRange(mainFaceArea,0.001f, 0.003f)) {
            return "veryLongShot";
        } else {
            return null;
        }
    }

    /** 获取“平均主人脸面积”，用于判断镜头类型 */
    public static float getAverMainFaceArea(List<FrameItem> frameBuffer, int mainFaceCount) {
        if(mainFaceCount == 0){
            return 0f;
        }
        float totalFaceAreas = 0;
        if(mainFaceCount == 1){
            List<Float> areas = new ArrayList<>();
            VisitServices.from(frameBuffer).visitForResultList(
                    new ResultVisitor<FrameItem, Float>() {
                        @Override
                        public Float visit(FrameItem fbi, Object param) {
                            return !fbi.areas.isEmpty() ? fbi.areas.get(0) : 0f;
                        }
                    }, areas);
                    // (fbi, param) -> fbi.areas.get(0), areas);
            totalFaceAreas = CollectionUtils.sum(areas);
        }else if(mainFaceCount == 2){
            List<Float> list = VisitServices.from(frameBuffer).map(new ResultVisitor<FrameItem, List<Float>>() {
                @Override
                public List<Float> visit(FrameItem fbi, Object param) {
                    return fbi.areas;
                }
            }).map(new ResultVisitor<List<Float>, Float>() {
                @Override
                public Float visit(List<Float> floats, Object param) {
                    if(floats.size() < 2){
                        return null;
                    }
                    return (floats.get(0) + floats.get(1));
                }
            }).visitForQueryList(Visitors.truePredicateVisitor(), null);
            totalFaceAreas = CollectionUtils.sum(list);
        }else{
            //统计前3张脸
            List<Float> list = VisitServices.from(frameBuffer).map(new ResultVisitor<FrameItem, List<Float>>() {
                @Override
                public List<Float> visit(FrameItem fbi, Object param) {
                    return fbi.areas;
                }
            }).map(new ResultVisitor<List<Float>, Float>() {
                @Override
                public Float visit(List<Float> floats, Object param) {
                    if(floats.size() < 3){
                        return null;
                    }
                    return floats.get(0) + floats.get(1) + floats.get(2);
                }
            }).visitForQueryList(Visitors.truePredicateVisitor(), null);
            totalFaceAreas = CollectionUtils.sum(list);
        }
        return totalFaceAreas / frameBuffer.size();
    }

    /** 主人脸个数， frameAreas 人脸面积数组 */
    public static int getMainFaceCount(List<Float> frameAreas) {
        int size = frameAreas.size();
        if(size <= 1){
            return size;
        }
        int mainFaces = size;
        Collections.sort(frameAreas, (o1, o2) -> Float.compare(o2, o1));
        //用人脸面积比区分 2-5
        if(size <= 3){
            int p_idx = 0;
            int q_idx = 1;
            while (q_idx < size) {
                if (frameAreas.get(p_idx) / frameAreas.get(q_idx) > MAIN_FACE_AREA_RATE ){
                    mainFaces = p_idx + 1;
                    break;
                }
                p_idx += 1;
                q_idx += 1;
            }
        }else{
            //用人脸面积均值区分
            float sum = CollectionUtils.sum(frameAreas);
            final float average = sum / size;
            for(int i = 0 ; i < size ; i ++){
                Float area = frameAreas.get(i);
                if ((area < average) && ( (average - area) / average > AVERAGE_AREA_DIFF_RATE)) {
                    mainFaces = i;
                    break;
                }
            }
        }
        return mainFaces;
    }
}
