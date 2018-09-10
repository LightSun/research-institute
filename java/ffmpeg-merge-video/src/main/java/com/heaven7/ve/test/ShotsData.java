package com.heaven7.ve.test;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.utils.StringMapGsonAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author heaven7
 */
public class ShotsData implements ShotAssigner{

    @JsonAdapter(StringMapGsonAdapter.class)
    @SerializedName("shot_types")
    private Map<String, String> shotTypeMap;

    @JsonAdapter(StringMapGsonAdapter.class)
    @SerializedName("shot_cuts")
    private Map<String, String> shotCutsMap;

    private ShotCuts shotCuts;

    public Map<String, String> getShotTypeMap() {
        return shotTypeMap;
    }
    public void setShotTypeMap(Map<String, String> shotTypeMap) {
        this.shotTypeMap = shotTypeMap;
    }

    @Override
    public int assignShotType(MediaPartItem item) {
        String fileName = FileUtils.getFileName(item.getItem().getFilePath());
        return MetaInfo.getShotTypeFrom(shotTypeMap.get(fileName));
    }

    @Override
    public List<MediaPartItem> assignShotCuts(ColorGapContext context, CutItemDelegate delegate) {
        if(shotCuts == null){
            shotCuts = new ShotCuts(shotCutsMap);
        }
        String fileName = FileUtils.getFileName(delegate.getItem().getFilePath());
        List<ShotCuts.CutInterval> intervals = shotCuts.getCutInterval(fileName);
        return VisitServices.from(intervals).map(new ResultVisitor<ShotCuts.CutInterval, MediaPartItem>() {
            @Override
            public MediaPartItem visit(ShotCuts.CutInterval interval, Object param) {
                return VEGapUtils.getShot(context, delegate, interval.getStartTimeInFrames(), interval.getEndTimeInFrames());
            }
        }).getAsList();
    }


}
