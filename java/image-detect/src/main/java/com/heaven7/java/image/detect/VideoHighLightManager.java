package com.heaven7.java.image.detect;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.image.Matrix2;
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

    public VideoHighLightManager(VideoFrameDelegate vfd, String videoSrc) {
        super(vfd, videoSrc);
    }

    public VideoHighLightManager(VideoFrameDelegate vfd, String videoSrc, int gap, ImageDetector detector) {
        super(vfd, videoSrc, gap, detector);
    }

    @Override
    protected void onDetect(ImageDetector detector, AbstractVideoManager.Callback<List<IHighLightData>> callback, int time, byte[] data) {
        detector.detectHighLight(data, new InternalCallback(time));
    }

    @Override
    protected void onDetectBatch(ImageDetector detector, Callback<List<IHighLightData>> callback, List<Integer> times, byte[] batchData) {
        detector.detectHighLightBatch(batchData, new InternalCallback(times));
    }

    public interface ScoreProvider {

        /**
         * compute the highlight score.
         *
         * @param data the highlight data
         * @return the common score
         */
        float getCommonScore(IHighLightData data);
    }

    public static class VideoHighLight {

        /**
         * key is frame time is seconds, value is high light data
         */
        private final SparseArray<IHighLightData> maxScoreData = new SparseArray<>();
        private final SparseArray<List<? extends IHighLightData>> dataMap;
        private final ScoreProvider provider;

        public VideoHighLight(ScoreProvider provider, SparseArray<List<? extends IHighLightData>> dataMap) {
            this.provider = provider;
            this.dataMap = dataMap;
        }

        public final SparseArray<List<? extends IHighLightData>> getDataMap() {
            return dataMap;
        }

        private int getHighLightPoint() {
            int maxScoreTime = -1;
            int size = dataMap.size();
            float score = -1;
            for (int i = 0; i < size; i++) {
                int time = dataMap.keyAt(i);
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
            int time = getHighLightPoint();
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

        private float computeCommonScore(int time) {
            List<IHighLightData> list = (List<IHighLightData>) dataMap.get(time);
            if (Predicates.isEmpty(list)) {
                return 0f;
            }
            return VisitServices.from(list).map(new ResultVisitor<IHighLightData, Float>() {
                @Override
                public Float visit(IHighLightData data, Object param) {
                    return provider.getCommonScore(data);
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
