package com.heaven7.ve.test;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.anno.CallOnce;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.CutItemDelegate;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.utils.MapGsonAdapter;
import com.vida.common.GsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heaven7
 */
public class ShotsData implements ShotAssigner{

    @JsonAdapter(ShotItemDataAdapter.class)
    private Map<String, ShotItemData> shots;

    private Map<String, List<ShotItemData>> multiShots;

    public Map<String, ShotItemData> getShots() {
        return shots;
    }
    public void setShots(Map<String, ShotItemData> shots) {
        this.shots = shots;
    }

    @CallOnce
    public void resolve(){
        final String symbol = "_";
        MapVisitService<String, ShotItemData> service = VisitServices.from(shots).filter(
                new MapPredicateVisitor<String, ShotItemData>() {
            @Override
            public Boolean visit(KeyValuePair<String, ShotItemData> pair, Object param) {
                return pair.getKey().contains(symbol);
            }
        }, null).fire(new MapFireVisitor<String, ShotItemData>() {
            @Override
            public Boolean visit(KeyValuePair<String, ShotItemData> pair, Object param) {
                //remove from single shots
                shots.remove(pair.getKey());
                return null;
            }
        });
        if(service.size() > 0){
            multiShots = new HashMap<>();
            service.fire(new MapFireVisitor<String, ShotItemData>() {
                @Override
                public Boolean visit(KeyValuePair<String, ShotItemData> pair, Object param) {
                    String[] strs = pair.getKey().split(symbol);
                    String key = strs[0];
                    multiShots.computeIfAbsent(key, k -> new ArrayList<>()).add(pair.getValue());
                    return null;
                }
            });
        }
    }

    @Override
    public String assignShotType(MediaPartItem item) {
        String fileName = FileUtils.getFileName(item.getItem().getFilePath());
        return getShotItemData(fileName, item).getShotType();
    }

    @Override
    public List<MediaPartItem> assignShotCuts(ColorGapContext context, CutItemDelegate delegate) {
        String fileName = FileUtils.getFileName(delegate.getItem().getFilePath());
        final List<CutInterval> intervals;
        ShotItemData data = shots.get(fileName);
        if(data != null) {
           intervals = data.getCutIntervals();
        }else{
            List<ShotItemData> list = multiShots.get(fileName);
            intervals = VisitServices.from(list).map(new ResultVisitor<ShotItemData, CutInterval>() {
                @Override
                public CutInterval visit(ShotItemData sid, Object param) {
                    return sid.getCutIntervals().get(0);
                }
            }).getAsList();
        }
        return VisitServices.from(intervals).map(new ResultVisitor<CutInterval, MediaPartItem>() {
            @Override
            public MediaPartItem visit(CutInterval interval, Object param) {
                return VEGapUtils.getShot(context, delegate, interval.getStartTimeInFrames(), interval.getEndTimeInFrames());
            }
        }).getAsList();
    }

    @Override
    public int assignMainFaceCount(MediaPartItem item) {
        String fileName = FileUtils.getFileName(item.getItem().getFilePath());
        return getShotItemData(fileName, item).getMainFaceCount();
    }

    @Override
    public int assignBodyCount(MediaPartItem item) {
        String fileName = FileUtils.getFileName(item.getItem().getFilePath());
        return getShotItemData(fileName, item).getBodyCount();
    }

    private ShotItemData getShotItemData(String fileName, MediaPartItem item) {
        ShotItemData data = shots.get(fileName);
        if(data == null){
            List<ShotItemData> list = multiShots.get(fileName);
            if(Predicates.isEmpty(list)){
                throw new IllegalStateException("can't find assign data for (file).mp4 % " + fileName);
            }
            data = VisitServices.from(list).query(new PredicateVisitor<ShotItemData>() {
                @Override
                public Boolean visit(ShotItemData sid, Object param) {
                    CutInterval interval = sid.getCutIntervals().get(0);
                    return interval.getStartTimeInFrames() == item.videoPart.getStartTime()
                            && interval.getEndTimeInFrames() == item.videoPart.getEndTime();
                }
            });
        }
        Throwables.checkNull(data);
        return data;
    }

    public static final class ShotItemDataAdapter extends MapGsonAdapter<ShotItemData>{

        @Override
        protected ShotItemData readValue(JsonReader in) throws IOException {
            ShotItemData data = new ShotItemData();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case ShotItemData.NAME_BODY_COUNT:
                        data.setBodyCount(in.nextInt());
                        break;

                    case ShotItemData.NAME_FACE_COUNT:
                        data.setMainFaceCount(in.nextInt());
                        break;

                    case ShotItemData.NAME_SHOT_CUT:
                        data.setShotCuts(in.nextString());
                        break;

                    case ShotItemData.NAME_SHOT_TYPE:
                        data.setShotType(in.nextString());
                        break;
                }
            }
            in.endObject();
            return data;
        }
        @Override
        protected void writeValue(JsonWriter out, ShotItemData value) throws IOException {
            out.beginObject();
            VisitServices.from(GsonUtils.toMap(value)).fire(new MapFireVisitor<String, String>() {
                @Override
                public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                    try {
                        out.name(pair.getKey()).value(pair.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            out.endObject();
        }
    }

}
