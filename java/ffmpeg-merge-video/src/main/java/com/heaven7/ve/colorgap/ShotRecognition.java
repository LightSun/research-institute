package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.kingdom.ModuleData;

import java.util.List;

import static com.heaven7.ve.colorgap.IShotRecognizer.*;
import static com.heaven7.ve.colorgap.MetaInfo.SHOT_TYPE_NONE;

/**
 * @author heaven7
 */
public class ShotRecognition {

    private static final String TAG = "ShotRecognition";

    public static final int THRESOLD_KEY_POINT_COUNT = 9;

    public static String getShotCategoryString(int shotCategory) {
        switch (shotCategory){
            case CATEGORY_ENV:
                return "CATEGORY_ENV";
            case CATEGORY_PART:
                return "CATEGORY_PART";
            case CATEGORY_PRODUCT:
                return "CATEGORY_PRODUCT";
        }
        return "unknown";
    }

    public static int parseShotCategory(String str){
        switch (str){
            case "enviroment":
                return CATEGORY_ENV;
            case "object":
                return CATEGORY_PRODUCT;
            case "portion":
                return CATEGORY_PART;
        }
        throw new UnsupportedOperationException("wrong shot category = " + str);
    }

    /***
     * recognize the shot category. such as {@linkplain IShotRecognizer#CATEGORY_ENV} and etc.
     * @param item the media part item
     * @return the shot category.
     */
    public static int recognizeShotCategory(MediaPartItem item) {
        int faceCount = item.imageMeta.getMainFaceCount();
        int keyPointCount = item.getKeyPointCount();
        if (faceCount > 0) {
            return CATEGORY_ENV;
        } else {
            return keyPointCount >= THRESOLD_KEY_POINT_COUNT ? CATEGORY_PART : CATEGORY_PRODUCT;
        }
    }

    /** return  {@linkplain MetaInfo#SHOT_TYPE_NONE} means can't find shot type . need request 'subject-identification' */
    public static int getShotType(MediaPartItem item) {
        String shotType = item.imageMeta.getShotType();
        int oldType = SHOT_TYPE_NONE;
        if (!TextUtils.isEmpty(shotType)) {
            oldType = MetaInfoUtils.getShotTypeFrom(shotType);
        }
        int bodyShotType = getShotTypeByBody(item);
        //trans shot_type and body shotType is none
        if(oldType == SHOT_TYPE_NONE && bodyShotType == SHOT_TYPE_NONE){
            return getShotTypeByHighLight(item);
        }else{
            if(oldType == MetaInfo.SHOT_TYPE_NONE){
                return bodyShotType;
            }else if(bodyShotType == MetaInfo.SHOT_TYPE_NONE){
                return oldType;
            }
            //get the nearest shot type
            return Math.min(oldType, bodyShotType);
        }
    }

    private static int getShotTypeByBody(MediaPartItem item) {
        //shot-type of body must > 9
        if(item.getKeyPointCount() < THRESOLD_KEY_POINT_COUNT){
            return MetaInfo.SHOT_TYPE_NONE;
        }
        float bodyArea = item.getBodyArea();
        float bodyRate = bodyArea / (item.imageMeta.getWidth() * item.imageMeta.getHeight());
        return getShopTypeByBody(bodyRate);
    }

    private static int getShotTypeByHighLight(MediaPartItem item) {
        KeyValuePair<Integer, List<IHighLightData>> highLight = item.getHighLight();
        if (highLight == null || Predicates.isEmpty(highLight.getValue())) {
            return SHOT_TYPE_NONE;
        }
        List<IHighLightData> data = highLight.getValue();
        //module shot type
        int shotType_module = SHOT_TYPE_NONE;
        if(!Predicates.isEmpty(data)){
            shotType_module = getShotTypeOfHighLightImpl(item, data);
        }
        return shotType_module; //may be SHOT_TYPE_NONE
    }

    private static int getShotTypeOfHighLightImpl(MediaPartItem item, List<IHighLightData> data) {
        IHighLightData hld = VisitServices.from(data).pile(new PileVisitor<IHighLightData>() {
            @Override
            public IHighLightData visit(Object o, IHighLightData hd1, IHighLightData hd2) {
                return hd1.getLocation().getArea() > hd2.getLocation().getArea() ? hd1 : hd2;
            }
        });
        ModuleData moduleData = item.getKingdom().getModuleData(hld.getName());
        if(moduleData == null){
            Logger.w(TAG , "getShotTypeOfHighLight", "no module data for name = " + hld.getName());
            return SHOT_TYPE_NONE;
        }
        int shotType = MetaInfoUtils.getShotTypeFrom(moduleData.getShotType());
        float areaRate = hld.getLocation().getArea() * 1f / (item.imageMeta.getWidth() * item.imageMeta.getHeight());
        if(CommonUtils.isInRange(areaRate, 0.5f, 1f)){
            //empty
        }else if(CommonUtils.isInRange(areaRate, 0.25f, 0.5f)){
            shotType += 1;
        }else if(CommonUtils.isInRange(areaRate, 0f, 0.25f)){
            shotType += 2;
        }
        return shotType;
    }

    private static int getShopTypeByBody(float area) {
        if (CommonUtils.isInRange(area, 0.274f, 0.354f)) {
            return MetaInfo.SHOT_TYPE_MEDIUM_SHOT;
        } else if (CommonUtils.isInRange(area, 0.15f, 0.274f)) {
            return MetaInfo.SHOT_TYPE_MEDIUM_LONG_SHOT;
        } else if (CommonUtils.isInRange(area, 0.0075f, 0.15f)) {
            return MetaInfo.SHOT_TYPE_LONG_SHORT;
        } else if (CommonUtils.isInRange(area, 0.0f, 0.0075f)) {
            return MetaInfo.SHOT_TYPE_BIG_LONG_SHORT;
        }
        return SHOT_TYPE_NONE;
    }

}
