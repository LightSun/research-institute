package com.heaven7.ve.test;

import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author heaven7
 */
public class ShotCuts {

    private final Map<String, List<CutInterval>> mShotCuts;

    public ShotCuts(Map<String, String> shotCutsMap) {
        this.mShotCuts = VisitServices.from(shotCutsMap).map2MapKey(
                new MapResultVisitor<String, String, List<CutInterval>>() {
            @Override
            public List<CutInterval> visit(KeyValuePair<String, String> t, Object param) {
                String value = t.getValue();
                String[] intervals = value.split(",");
                return  VisitServices.from(Arrays.asList(intervals)).map(new ResultVisitor<String, CutInterval>() {
                    @Override
                    public CutInterval visit(String s, Object param) {
                        String[] strs = s.split("-");
                        assert strs.length == 2;
                        return new CutInterval(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]));
                    }
                }).getAsList();
            }
        }).get().toNormalMap();
    }

    public List<CutInterval> getCutInterval(String fn){
        return mShotCuts.get(fn);
    }

    public static class CutInterval{
        final int startTime; //in seconds
        final int endTime;
        public CutInterval(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public long getStartTimeInFrames(){
            return CommonUtils.timeToFrame(startTime, TimeUnit.SECONDS);
        }

        public long getEndTimeInFrames(){
            return CommonUtils.timeToFrame(endTime, TimeUnit.SECONDS);
        }
    }
}
