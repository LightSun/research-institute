package com.heaven7.ve.starter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.visitor.FireMultiVisitor2;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.cross_os.VEFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * the music cut data loader
 *
 * @author heaven7
 */
public class MusicCutStarter implements IStarter, MusicCutter {

    private int start = 1; //include index
    private int end = 48; //include index
    private final HashMap<String, MusicCutData> mMap = new HashMap<>();
    private final MusicPathProvider provider;

    public MusicCutStarter(MusicPathProvider provider) {
        this.provider = provider;
    }

    @Override
    public void init(Context context, Object param) {
        for (int i = end; i >= start; i--) {
            MusicCutData data = loadMusicCut(context, i);
            assert data != null;
            String musicPath = provider.getMusicPath(data);
            mMap.put(musicPath, data);
        }
        VEGapUtils.asColorGapContext(context).setMusicCutter(this);
    }

    private static MusicCutData loadMusicCut(Context context, int index) {
        String resName = "M" + index + ".json";
        String json = ResourceLoader.getDefault().loadFileAsString(context, "table/music_cut/" + resName);
        MusicCutData data = new Gson().fromJson(json, MusicCutData.class);
        data.setName(index + "");
        return data;
    }

    @Override
    public CutInfo[] cut(Context context, String[] musicPath) {
        List<CutInfo> list = VisitServices.from(Arrays.asList(musicPath))
                .map(new ResultVisitor<String, CutInfo>() {
                    @Override
                    public CutInfo visit(String s, Object param) {
                        MusicCutData data = mMap.get(s);
                        List<IPlaidInfo> cuts = data.getCuts(context, s);
                        CutInfo info = new CutInfo();
                        info.setPlaidInfos(cuts);
                        return info;
                    }
                }).getAsList();
        return list.toArray(new CutInfo[list.size()]);
    }

    /**
     * convert the cuts
     * @param musicPath the music file path
     * @param cuts the cuts. like '1.1,1.5,1.6'
     * @param duration the target duration (of video) to fit.
     * @return the cut plaids.
     */
    public static List<IPlaidInfo> convertCuts(String musicPath, String cuts, int duration){
        String[] strs = cuts.split(",");
        CollectionVisitService<Float> service = VisitServices.from(Arrays.asList(strs))
                .map(new ResultVisitor<String, Float>() {
                    @Override
                    public Float visit(String s, Object param) {
                        return Float.valueOf(s);
                    }
                });
        final Float maxTime = service.getAsList().get(service.size() - 1);
        List<TimeInterval> intervals = new ArrayList<>();
        service.asListService().fireMulti2(2, 1, null, new FireMultiVisitor2<Float>() {
            @Override
            public boolean visit(Object param, int count, int step, List<Float> floats) {
                //1.5  3.2  4.8  5.7  8.2  9.8 12 . need 10 seconds.   10-9.8 < 1s so last interval is 8.2-10
                if (floats.size() < 2) {
                    return true;
                }
                Float start = floats.get(0);
                Float end = floats.get(1);
                if (start >= duration) {
                    return true;
                }
                // in this interval.
                if (duration <= end) {
                    if (duration - start < 1) {
                        //make last interval end time to duration.
                        if (intervals.isEmpty()) throw new IllegalStateException("duration = " + duration);
                        intervals.get(intervals.size() - 1).setEnd(duration);
                    } else {
                        intervals.add(new TimeInterval(start, duration));
                    }
                    return true;
                }
                //not reached max internal
                intervals.add(new TimeInterval(start, end));
                return false;
            }
        });

        return VisitServices.from(intervals).map(new ResultVisitor<TimeInterval, IPlaidInfo>() {
            @Override
            public IPlaidInfo visit(TimeInterval interval, Object param) {
                return interval.toPlaid(musicPath, maxTime);
            }
        }).getAsList();
    }

    public static class MusicCutData {
        @SerializedName("music")
        private String music;
        private String cuts;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<IPlaidInfo> getCuts(Context ctx, String musicPath) {
            final ColorGapContext context = (ColorGapContext) ctx;
            final int duration = context.getMontageParameter().getDuration();
            // String fileName = FileUtils.getFileName(musicPath);
            return convertCuts(musicPath, cuts , duration);
        }
    }

    private static class TimeInterval {
        //start and end time in seconds
        final float start;
        float end;

        TimeInterval(float start, float end) {
            this.start = start;
            this.end = end;
        }

        public void setEnd(float end) {
            this.end = end;
        }

        public IPlaidInfo toPlaid(String musicPath, float maxTime) {
            IPlaidInfo info = VEFactory.getDefault().newPlaidInfo();
            info.setStartTime(CommonUtils.timeToFrame(start, TimeUnit.SECONDS));
            info.setEndTime(CommonUtils.timeToFrame(end, TimeUnit.SECONDS));
            info.setMaxDuration(CommonUtils.timeToFrame(maxTime, TimeUnit.SECONDS));
            info.setPath(musicPath);
            return info;
        }
    }
}
