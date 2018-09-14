package com.heaven7.ve.utils;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.image.detect.HighLightArea;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.impl.ScoreProviderImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * the high-light data helper
 * @author heaven7
 */
public class HighLightHelper {

    private final SparseArray<List<? extends IHighLightData>> highLightMap;
    private final boolean image;

    public HighLightHelper(SparseArray<List<? extends IHighLightData>> highLightMap, boolean image) {
        this.highLightMap = highLightMap;
        this.image = image;
    }

    @SuppressWarnings("unchecked")
    public KeyValuePair<Integer, List<IHighLightData>> getHighLight(int time){
        if(highLightMap == null){
            return null;
        }
        List<IHighLightData> data = (List<IHighLightData>) highLightMap.get(time);
        if(Predicates.isEmpty(data)){
            return null;
        }
        return KeyValuePair.create(time, data);
    }
    @SuppressWarnings("unchecked")
    public KeyValuePair<Integer, List<IHighLightData>> getHighLight(ColorGapContext context, TimeTraveller tt){
        if(highLightMap == null){
            return null;
        }
        //image
        if(image){
            return getHighLight(0);
        }
        //video
        int start = (int) CommonUtils.frameToTime(tt.getStartTime(), TimeUnit.SECONDS);
        int end = (int)CommonUtils.frameToTime(tt.getEndTime(), TimeUnit.SECONDS);
        VideoHighLight vhl = new VideoHighLight(context,
                new ScoreProviderImpl(), highLightMap);
        int time = vhl.getHighLightPoint(start, end);
        if(time == -1){
            return null;
        }
        List<IHighLightData> data = (List<IHighLightData>) highLightMap.get(time);
        if(Predicates.isEmpty(data)){
            return null;
        }
        return KeyValuePair.create(time, data);
    }
    @SuppressWarnings("unchecked")
    public List<KeyValuePair<Integer, List<IHighLightData>>> getHighLights(ColorGapContext context, TimeTraveller tt){
        if(highLightMap == null){
            return null;
        }
        if(image){
            KeyValuePair<Integer, List<IHighLightData>> light = getHighLight(0);
            if(light != null){
                return new ArrayList<>(Arrays.asList(light));
            }else{
                return null;
            }
        }
        int start = (int)CommonUtils.frameToTime(tt.getStartTime(), TimeUnit.SECONDS);
        int end = (int)CommonUtils.frameToTime(tt.getEndTime(), TimeUnit.SECONDS);
        List<KeyValuePair<Integer, List<IHighLightData>>> result = new ArrayList<>();
        for (int i = start ; i <= end ; i ++){
            List<IHighLightData> data = (List<IHighLightData>) highLightMap.get(i);
            if(!Predicates.isEmpty(data)){
                result.add(KeyValuePair.create(i, data));
            }
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    public HighLightArea getHighLightArea(ColorGapContext context, TimeTraveller tt){
        if(highLightMap == null){
            return null;
        }
        if(image){
            if(getHighLight(0) != null){
                HighLightArea area = new HighLightArea();
                area.setEndTime(0);
                area.setEndTime(0);
                return area;
            }else {
                return null;
            }
        }
        int start = (int) CommonUtils.frameToTime(tt.getStartTime(), TimeUnit.SECONDS);
        int end = (int)CommonUtils.frameToTime(tt.getEndTime(), TimeUnit.SECONDS);
        VideoHighLight vhl = new VideoHighLight(context,
                new ScoreProviderImpl(), highLightMap);
        return vhl.getHighLightArea(start, end);
    }


}
