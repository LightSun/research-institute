package com.heaven7.ve.test;

import com.google.gson.annotations.SerializedName;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.vida.common.GsonUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author heaven7
 */
public class ShotItemData implements GsonUtils.IRawData{

    public static final String NAME_SHOT_TYPE  = "shot_type";
    public static final String NAME_SHOT_CUT   = "shot_cut";
    public static final String NAME_FACE_COUNT = "main_face_count";
    public static final String NAME_BODY_COUNT = "body_count";

    @SerializedName(NAME_SHOT_TYPE)
    private String shotType;

    @SerializedName(NAME_SHOT_CUT)
    private String shotCuts;

    @SerializedName(NAME_FACE_COUNT)
    private int mainFaceCount;

    @SerializedName(NAME_BODY_COUNT)
    private int bodyCount;

    public String getShotType() {
        return shotType;
    }
    public void setShotType(String shotType) {
        this.shotType = shotType;
    }
    public String getShotCuts() {
        return shotCuts;
    }
    public void setShotCuts(String shotCuts) {
        this.shotCuts = shotCuts;
    }
    public int getMainFaceCount() {
        return mainFaceCount;
    }
    public void setMainFaceCount(int mainFaceCount) {
        this.mainFaceCount = mainFaceCount;
    }
    public int getBodyCount() {
        return bodyCount;
    }
    public void setBodyCount(int bodyCount) {
        this.bodyCount = bodyCount;
    }

    public List<CutInterval> getCutIntervals(){
        String[] intervals = shotCuts.split(",");
        return VisitServices.from(Arrays.asList(intervals)).map(new ResultVisitor<String, CutInterval>() {
            @Override
            public CutInterval visit(String s, Object param) {
                String[] strs = s.split("-");
                assert strs.length == 2;
                return new CutInterval(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]));
            }
        }).getAsList();
    }
}
