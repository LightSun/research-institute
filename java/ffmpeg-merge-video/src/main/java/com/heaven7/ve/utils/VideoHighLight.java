package com.heaven7.ve.utils;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.image.detect.HighLightArea;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.VideoHighLightManager;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

/*public*/ class VideoHighLight {

    private final SparseArray<IHighLightData> maxScoreData = new SparseArray<>();
    private final SparseArray<List<? extends IHighLightData>> dataMap;
    private final VideoHighLightManager.ScoreProvider provider;
    private final Object context;

    public VideoHighLight(Object context, VideoHighLightManager.ScoreProvider provider, SparseArray<List<? extends IHighLightData>> dataMap) {
        this.context = context;
        this.provider = provider;
        this.dataMap = dataMap;
    }

    public final SparseArray<List<? extends IHighLightData>> getDataMap() {
        return this.dataMap;
    }

    public int getHighLightPoint(int stInSeconds, int etInSeconds) {
        int maxScoreTime = -1;
        int size = this.dataMap.size();
        float score = -1.0F;
        //in case high-light time start or end. (1 seconds offset)
        for (int i = 0; i < size; ++i) {
            int time = this.dataMap.keyAt(i);
            if (time >= stInSeconds && time <= etInSeconds) {
                //for some case may
                if(time <= stInSeconds + 1 || time >= etInSeconds - 1){
                    continue;
                }
                float tmpScore = this.computeCommonScore(time) + this.maxConfidenceData(time).getScore();
                if (tmpScore > score) {
                    score = tmpScore;
                    maxScoreTime = time;
                }
            }
        }

        return maxScoreTime;
    }

    public HighLightArea getHighLightArea(int stInSeconds, int etInSeconds) {
        int time = this.getHighLightPoint(stInSeconds, etInSeconds);
        if (time == -1) {
            return null;
        } else {
            //check blow
            int blow = --time;
            for (; ; blow--) {
                if (dataMap.get(blow) == null) {
                    break;
                }
            }
            //check above
            int above = ++time;
            for (; ; above++) {
                if (dataMap.get(above) == null) {
                    break;
                }
            }
            HighLightArea area = new HighLightArea();
            area.setStartTime(blow + 1);
            area.setEndTime(above - 1);
            return area;
        }
    }

    @SuppressWarnings("unchecked")
    public float computeCommonScore(int time) {
        List<IHighLightData> list = (List) this.dataMap.get(time);
        return Predicates.isEmpty(list) ? 0.0F : VisitServices.from(list).map(new ResultVisitor<IHighLightData, Float>() {
            public Float visit(IHighLightData data, Object param) {
                return VideoHighLight.this.provider.getCommonScore(VideoHighLight.this.context, data);
            }
        }).pile(new PileVisitor<Float>() {
            public Float visit(Object o, Float f1, Float f2) {
                return f1 + f2;
            }
        });
    }
    @SuppressWarnings("unchecked")
    private IHighLightData maxConfidenceData(int time) {
        IHighLightData oldData = this.maxScoreData.get(time);
        if (oldData == null) {
            List<IHighLightData> list = (List) this.dataMap.get(time);
            oldData = VisitServices.from(list).pile(new PileVisitor<IHighLightData>() {
                public IHighLightData visit(Object o, IHighLightData hd1, IHighLightData hd2) {
                    return hd1.getScore() > hd2.getScore() ? hd1 : hd2;
                }
            });
            this.maxScoreData.put(time, oldData);
        }
        return oldData;
    }
}
