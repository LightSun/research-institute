package com.heaven7.ve.test;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.utils.StringMapGsonAdapter;

import java.util.Map;

/**
 * @author heaven7
 */
public class ShotsData implements ShotAssigner{

    @JsonAdapter(StringMapGsonAdapter.class)
    @SerializedName("shot_types")
    private Map<String, String> shotTypeMap;

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
}
