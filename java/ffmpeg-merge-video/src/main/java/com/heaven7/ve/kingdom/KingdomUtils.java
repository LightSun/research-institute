package com.heaven7.ve.kingdom;

import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;
import java.util.Map;

/**
 * @author heaven7
 */
/*public*/ class KingdomUtils {

    public static List<Integer> getTagIds(Map<String, List<TagItem>> map, String tag) {
        List<TagItem> items = map.get(tag);
        if (items == null) {
            return null;
        }
        return getIds(items);
    }

    public static TagItem getTagItem(Map<String, List<TagItem>> map, int index) {
        for (List<TagItem> list : map.values()) {
            for (TagItem item : list) {
                if (item.getIndex() == index) {
                    return item;
                }
            }
        }
        return null;
    }

    public static List<Integer> getIds(List<TagItem> list) {
        return VisitServices.from(list).map((tagItem, param) -> tagItem.getIndex()).getAsList();
    }
}
