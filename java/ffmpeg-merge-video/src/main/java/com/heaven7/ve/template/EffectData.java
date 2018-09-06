package com.heaven7.ve.template;

import com.google.gson.annotations.SerializedName;
import com.heaven7.java.base.util.Predicates;

import java.util.List;

/**
 * the effect data from file
 * @author heaven7
 */
public class EffectData {

    //effect weight: index > chapter > normal level
    @SerializedName("texture")
    private Data textureData;
    @SerializedName("transition")
    private Data transitionData;
    @SerializedName("filter")
    private Data filterData;
    @SerializedName("special_effect")
    private Data specialEffectData;

    public Data getTextureData() {
        return textureData;
    }
    public void setTextureData(Data textureData) {
        this.textureData = textureData;
    }

    public Data getTransitionData() {
        return transitionData;
    }
    public void setTransitionData(Data transitionData) {
        this.transitionData = transitionData;
    }

    public Data getFilterData() {
        return filterData;
    }
    public void setFilterData(Data filterData) {
        this.filterData = filterData;
    }

    public Data getSpecialEffectData() {
        return specialEffectData;
    }
    public void setSpecialEffectData(Data specialEffectData) {
        this.specialEffectData = specialEffectData;
    }

    //weight: index > chapter > level
    public static class Data{
        @SerializedName("index")
        private List<Item> indexItems;
        @SerializedName("chapter")
        private List<Item> chapterItems;
        @SerializedName("level")
        private List<Item> levelItems;

        public boolean isValid(){
            return (!Predicates.isEmpty(indexItems) || !Predicates.isEmpty(chapterItems)
                    || !Predicates.isEmpty(levelItems));
        }

        public List<Item> getIndexItems() {
            return indexItems;
        }
        public void setIndexItems(List<Item> indexItems) {
            this.indexItems = indexItems;
        }

        public List<Item> getChapterItems() {
            return chapterItems;
        }
        public void setChapterItems(List<Item> chapterItems) {
            this.chapterItems = chapterItems;
        }

        public List<Item> getLevelItems() {
            return levelItems;
        }
        public void setLevelItems(List<Item> levelItems) {
            this.levelItems = levelItems;
        }
    }

    public static class Item{
        private int index ;        // -1 mean last
        private int level = -1;

        private float percentage;
        private List<String> values;

        //must set in texture
        private String type; //often is the dir of texture
        private String value; //for texture this is file name.

       // private boolean applyAll; //applyEffects all or not, this often used for chapter

        public int getLevel() {
            return level;
        }
        public void setLevel(int level) {
            this.level = level;
        }

        public float getPercentage() {
            return percentage;
        }
        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public List<String> getValues() {
            return values;
        }
        public void setValues(List<String> values) {
            this.values = values;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }

        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
    }
}
