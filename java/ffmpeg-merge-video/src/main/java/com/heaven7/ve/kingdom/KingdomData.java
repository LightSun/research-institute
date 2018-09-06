package com.heaven7.ve.kingdom;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.ve.anno.FileResource;
import com.heaven7.ve.utils.FloatMapGsonAdapter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * the kingdom data.
 * @author heaven7
 */
public class KingdomData {

    private List<MapData> maps;
    private List<String> subjects;
    private List<ModuleData> moduleDatas; // the configed module data

    @JsonAdapter(FloatMapGsonAdapter.class)
    @SerializedName("shot_type_score")
    private Map<String, Float> shotTypeMap;
    @JsonAdapter(FloatMapGsonAdapter.class)
    @SerializedName("main_face_score")
    private Map<String, Float> mainFaceMap;

    public Map<String, Float> getShotTypeMap() {
        return shotTypeMap;
    }
    public void setShotTypeMap(Map<String, Float> shotTypeMap) {
        this.shotTypeMap = shotTypeMap;
    }

    public Map<String, Float> getMainFaceMap() {
        return mainFaceMap;
    }
    public void setMainFaceMap(Map<String, Float> mainFaceMap) {
        this.mainFaceMap = mainFaceMap;
    }

    public List<ModuleData> getModuleDatas() {
        return moduleDatas;
    }
    public void setModuleDatas(List<ModuleData> moduleDatas) {
        this.moduleDatas = moduleDatas;
    }

    public List<MapData> getMaps() {
        return maps;
    }
    public void setMaps(List<MapData> maps) {
        this.maps = maps;
    }

    public List<String> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public static class MapData {
        private int type;
        private List<TagItemData> itemDatas;

        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }

        public List<TagItemData> getItemDatas() {
            return itemDatas;
        }
        public void setItemDatas(List<TagItemData> itemDatas) {
            this.itemDatas = itemDatas;
        }
    }
    public static class TagItemData {
        private String tag;
        private List<TagItem> items;

        public String getTag() {
            return tag;
        }
        public void setTag(String tag) {
            this.tag = tag;
        }
        public List<TagItem> getItems() {
            return items;
        }
        public void setItems(List<TagItem> items) {
            this.items = items;
        }
    }

}
    