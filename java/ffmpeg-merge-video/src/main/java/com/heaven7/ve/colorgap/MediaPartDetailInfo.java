package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.kingdom.ModuleData;

import java.util.List;

/**
 * the media part detail info.often used for debug
 *
 * @author heaven7
 */
public class MediaPartDetailInfo {

    private final MediaPartItem item;

    public MediaPartDetailInfo(MediaPartItem item) {
        this.item = item;
    }

    public Location getSubjectLocation() {
        return item.imageMeta.getSubjectLocation();
    }
    public int getMainFaceCount() {
        return item.imageMeta.getMainFaceCount();
    }
    public int getBodyCount(){
        return item.imageMeta.getBodyCount();
    }
    /**
     * -1 , means no body
     */
    public float getBodyRate() {
        MetaInfo.ImageMeta meta = item.getImageMeta();
        if (meta.getWidth() == 0 || meta.getHeight() == 0) {
            throw new IllegalStateException("must set width and height. for part = " + item);
        }
        if(item.getKeyPointCount() < ShotRecognition.THRESOLD_KEY_POINT_COUNT){
            return -1;
        }
        float bodyArea = item.getBodyArea();
        if (bodyArea == -1) {
            return -1;
        }
        return bodyArea / (meta.getWidth() * meta.getHeight());
    }

    public int getKeyFrameTime(){
        return item.getKeyFrameTime();
    }

    public String getShotType() {
        return item.imageMeta.getShotType();
    }

    public List<Integer> getTopTags() {
        List<List<Integer>> tags = item.imageMeta.getTags();
        if (tags == null) {
            return null;
        }
        return tags.get(0);
    }
    public String getTopTagsString() {
        List<Integer> topTags = getTopTags();
        if(Predicates.isEmpty(topTags)){
            return null;
        }
        List<String> list = VisitServices.from(topTags).map(new ResultVisitor<Integer, String>() {
            @Override
            public String visit(Integer index, Object param) {
                return Kingdom.getTagStr(index);
            }
        }).getAsList();
        return VisitServices.from(list).joinToString(": ");
    }
    public int getShotCategory(){
        return item.imageMeta.getShotCategory();
    }
    public String getShotCategoryString(){
        return ShotRecognition.getShotCategoryString(getShotCategory());
    }
    public List<IHighLightData> getHighLightData(){
        KeyValuePair<Integer, List<IHighLightData>> highLight = item.getHighLight();
        if(highLight == null || Predicates.isEmpty(highLight.getValue())){
            return null;
        }
        return highLight.getValue();
    }
    public String getHighLightString(){
        List<IHighLightData> highLightData = getHighLightData();
        if(highLightData == null){
            return null;
        }
        return VisitServices.from(highLightData).map(new ResultVisitor<IHighLightData, String>() {
            @Override
            public String visit(IHighLightData data, Object param) {
                return data.getName();
            }
        }).asListService().joinToString(": ");
    }
    public Integer getLevel() {
        ModuleData data = item.getHighLightModuleData();
        if(data == null){
            return null;
        }
        return data.getLevel();
    }

    @Override
    public String toString() {
        try {
            return "MediaPartDetailInfo{" +
                    "mainFaceCount = " + getMainFaceCount() +
                    ", bodyCount = " + getBodyCount() +
                    ", bodyRate = " + getBodyRate() +
                    ", shotType = " + getShotType() +
                    ", topTags = " + getTopTagsString() +
                    ", shotCategory = " + getShotCategoryString() +
                    ", highLight = " + getHighLightString() +
                    ", level = " + getLevel() +
                    ", keyFrameTime = " + getKeyFrameTime() +
                    '}';
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
