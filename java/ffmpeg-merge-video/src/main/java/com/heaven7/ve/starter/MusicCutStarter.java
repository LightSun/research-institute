package com.heaven7.ve.starter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.visitor.FireMultiVisitor;
import com.heaven7.java.visitor.FireMultiVisitor2;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.*;
import com.vida.common.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * the music cut data loader
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
        for (int i = end ; i >= start ; i --){
            MusicCutData data = loadMusicCut(i);
            assert data != null;
            String musicPath = provider.getMusicPath(data);
            mMap.put(musicPath, data);
        }
        VEGapUtils.asColorGapContext(context).setMusicCutter(this);
    }

    private static MusicCutData loadMusicCut(int index) {
        Reader reader = null;
        try {
            String resName = "M" + index + ".json";
            String json = IOUtils.readString(reader = new InputStreamReader(
                    ConfigUtil.loadResourcesAsStream("table/music_cut/" + resName)));
            MusicCutData data = new Gson().fromJson(json, MusicCutData.class);
            data.setName(index +"");
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public CutInfo[] cut(Context context, String[] musicPath) {
        List<CutInfo> list = VisitServices.from(Arrays.asList(musicPath)).map(new ResultVisitor<String, CutInfo>() {
            @Override
            public CutInfo visit(String s, Object param) {
                MusicCutData data = mMap.get(s);
                List<CutInfo.PlaidInfo> cuts = data.getCuts(context, s);
                CutInfo info = new CutInfo();
                info.setPlaidInfos(cuts);
                return info;
            }
        }).getAsList();
        return list.toArray(new CutInfo[list.size()]);
    }

    public static class MusicCutData{
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
        public List<CutInfo.PlaidInfo> getCuts(Context ctx, String musicPath){
            final ColorGapContext context = (ColorGapContext) ctx;
            final int duration = context.getMontageParameter().getDuration();
            // String fileName = FileUtils.getFileName(musicPath);
            String[] strs = cuts.split(",");
            List<CutInfo.PlaidInfo> result = new ArrayList<>();
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
                    Float start = floats.get(0);
                    Float end = floats.get(1);
                    if(start >= duration){
                        return true;
                    }
                    // in this interval.
                    if(duration <= end){
                        if(duration - start < 1){
                            //make last interval end time to duration.
                            if(intervals.isEmpty()) throw new IllegalStateException("duration = " + duration);
                            intervals.get(intervals.size() - 1).setEnd(duration);
                        }else{
                            intervals.add(new TimeInterval(start, duration));
                        }
                        return true;
                    }
                    //not reached max internal
                    intervals.add(new TimeInterval(start, end));
                    return false;
                }
            });

            return VisitServices.from(intervals).map(new ResultVisitor<TimeInterval, CutInfo.PlaidInfo>() {
                @Override
                public CutInfo.PlaidInfo visit(TimeInterval interval, Object param) {
                    return interval.toPlaid(musicPath, maxTime);
                }
            }).getAsList();
        }
    }

    private static class TimeInterval{
        //start and end time in seconds
        final float start;
        float end;
        public TimeInterval(float start, float end) {
            this.start = start;
            this.end = end;
        }

        public void setEnd(float end){
            this.end = end;
        }

        public CutInfo.PlaidInfo toPlaid(String musicPath, float maxTime){
            CutInfo.PlaidInfo info = new CutInfo.PlaidInfo();
            info.setStartTime(CommonUtils.timeToFrame(start, TimeUnit.SECONDS));
            info.setEndTime(CommonUtils.timeToFrame(end, TimeUnit.SECONDS));
            info.setMaxDuration(CommonUtils.timeToFrame(maxTime, TimeUnit.SECONDS));
            info.setPath(musicPath);
            return info;
        }
    }
}
