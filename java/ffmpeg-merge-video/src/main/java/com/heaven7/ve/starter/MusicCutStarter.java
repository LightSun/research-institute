package com.heaven7.ve.starter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicCutter;
import com.heaven7.ve.colorgap.MusicPathProvider;
import com.heaven7.ve.colorgap.VEGapUtils;
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
                List<CutInfo.PlaidInfo> cuts = data.getCuts(s);
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
        public List<CutInfo.PlaidInfo> getCuts(String musicPath){
           // String fileName = FileUtils.getFileName(musicPath);
            String[] strs = cuts.split(",");
            List<CutInfo.PlaidInfo> result = new ArrayList<>();
            CollectionVisitService<Float> service = VisitServices.from(Arrays.asList(strs)).map(new ResultVisitor<String, Float>() {
                @Override
                public Float visit(String s, Object param) {
                    return Float.valueOf(s);
                }
            });
            //last val is max
            final Float lastVal = service.getAsList().get(service.size() - 1);
            service.pile(new PileVisitor<Float>() {
                @Override
                public Float visit(Object o, Float val1, Float val2) {
                    CutInfo.PlaidInfo info = new CutInfo.PlaidInfo();
                    info.setStartTime(CommonUtils.timeToFrame(val1, TimeUnit.SECONDS));
                    info.setEndTime(CommonUtils.timeToFrame(val2, TimeUnit.SECONDS));
                    info.setMaxDuration(CommonUtils.timeToFrame(lastVal, TimeUnit.SECONDS));
                    info.setPath(musicPath);
                    result.add(info);
                    return val2;
                }
            });
            return result;
        }
    }
}
