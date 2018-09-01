package com.heaven7.ve.template;

import com.google.gson.annotations.JsonAdapter;
import com.heaven7.ve.utils.StringMapGsonAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author heaven7
 */
public class TemplateData {

    private String type;
    private List<Item> items;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {

        private float proportion; //比例
        private String relationship; // or/and
        @JsonAdapter(StringMapGsonAdapter.class)
        private Map<String, String> cases;

        public float getProportion() {
            return proportion;
        }
        public void setProportion(float proportion) {
            this.proportion = proportion;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public Map<String, String> getCases() {
            return cases;
        }

        public void setCases(Map<String, String> cases) {
            this.cases = cases;
        }
    }

}
