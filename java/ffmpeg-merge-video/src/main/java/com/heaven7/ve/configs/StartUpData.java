package com.heaven7.ve.configs;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.utils.StringMapGsonAdapter;

import java.util.Map;

/**
 * @author heaven7
 */
public class StartUpData {

    @JsonAdapter(StringMapGsonAdapter.class)
    @SerializedName("initMap")
    private Map<String, String> initMap;

    @SerializedName("use_type")
    private int type = ColorGapContext.TEST_TYPE_LOCAL;

    public Map<String, String> getInitMap() {
        return initMap;
    }
    public void setInitMap(Map<String, String> initMap) {
        this.initMap = initMap;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
