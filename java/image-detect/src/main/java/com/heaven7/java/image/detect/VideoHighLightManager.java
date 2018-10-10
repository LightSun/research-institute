package com.heaven7.java.image.detect;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.image.Matrix2;
import com.heaven7.java.image.utils.TransformUtils;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

/**
 * the video high light manager.
 *
 * @author heaven7
 */
public class VideoHighLightManager extends AbstractVideoManager<List<IHighLightData>> {

    public VideoHighLightManager(String videoSrc) {
        super(videoSrc);
    }

    public VideoHighLightManager( String videoSrc, int gap) {
        super( videoSrc, gap);
    }

    @Override
    protected void onDetect(ImageDetector detector, AbstractVideoManager.Callback<List<IHighLightData>> callback, int time, byte[] data) {
        detector.detectHighLight(data, new InternalCallback(time));
    }

    @Override
    protected void onDetectBatch(BatchInfo info,ImageDetector detector, Callback<List<IHighLightData>> callback, List<Integer> times, byte[] batchData) {
        detector.detectHighLightBatch(info, batchData, new InternalCallback(times));
    }

    @Override
    protected List<IHighLightData> transformData(List<IHighLightData> list, TransformInfo tInfo) {
        return TransformUtils.transformData(list, tInfo);
    }

    public interface ScoreProvider {

        /**
         * compute the highlight score.
         *
         * @param data the highlight data
         * @return the common score
         */
        float getCommonScore(Object context, IHighLightData data);
    }

    public static class VideoHighLight {

        /**
         * key is frame time is seconds, value is high light data
         */
        private final SparseArray<IHighLightData> maxScoreData = new SparseArray<>();
        private final SparseArray<List<? extends IHighLightData>> dataMap;
        private final ScoreProvider provider;
        private final Object context;

        public VideoHighLight(Object context, ScoreProvider provider, SparseArray<List<? extends IHighLightData>> dataMap) {
            this.context = context;
            this.provider = provider;
            this.dataMap = dataMap;
        }

        public final SparseArray<List<? extends IHighLightData>> getDataMap() {
            return dataMap;
        }
        public int getHighLightPoint() {
            return getHighLightPoint(0, Integer.MAX_VALUE);
        }
        /**  if not found return -1 */
        public int getHighLightPoint(int stInSeconds, int etInSeconds) {
            int maxScoreTime = -1;
            int size = dataMap.size();
            float score = -1;
            for (int i = 0; i < size; i++) {
                int time = dataMap.keyAt(i);
                if(time < stInSeconds || time > etInSeconds){
                    continue;
                }
                //common score + max confidence data.
                float tmpScore = computeCommonScore(time) + maxConfidenceData(time).getScore();
                if (tmpScore > score) {
                    score = tmpScore;
                    maxScoreTime = time;
                }
            }
            return maxScoreTime;
        }
        public HighLightArea getHighLightArea() {
            return getHighLightArea(0, Integer.MAX_VALUE);
        }
        public HighLightArea getHighLightArea(int stInSeconds, int etInSeconds) {
            int time = getHighLightPoint(stInSeconds, etInSeconds);
            if (time == -1) {
                return null;
            }
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

        @SuppressWarnings("unchecked")
        public float computeCommonScore(int time) {
            List<IHighLightData> list = (List<IHighLightData>) dataMap.get(time);
            if (Predicates.isEmpty(list)) {
                return 0f;
            }
            return VisitServices.from(list).map(new ResultVisitor<IHighLightData, Float>() {
                @Override
                public Float visit(IHighLightData data, Object param) {
                    return provider.getCommonScore(context, data);
                }
            }).pile(new PileVisitor<Float>() {
                @Override
                public Float visit(Object o, Float f1, Float f2) {
                    return f1 + f2;
                }
            });
        }

        @SuppressWarnings("unchecked")
        private IHighLightData maxConfidenceData(int time) {
            IHighLightData oldData = maxScoreData.get(time);
            if (oldData == null) {
                List<IHighLightData> list = (List<IHighLightData>) dataMap.get(time);
                oldData = VisitServices.from(list).pile(new PileVisitor<IHighLightData>() {
                    @Override
                    public IHighLightData visit(Object o, IHighLightData hd1, IHighLightData hd2) {
                        return hd1.getScore() > hd2.getScore() ? hd1 : hd2;
                    }
                });
                maxScoreData.put(time, oldData);
            }
            return oldData;
        }
    }

}
