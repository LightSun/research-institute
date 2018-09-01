package com.heaven7.ve.colorgap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.kingdom.ModuleData;

import java.util.List;

import static com.heaven7.ve.colorgap.IShotRecognizer.CATEGORY_ENV;
import static com.heaven7.ve.colorgap.IShotRecognizer.CATEGORY_PART;
import static com.heaven7.ve.colorgap.IShotRecognizer.CATEGORY_PRODUCT;
import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * @author heaven7
 */
public class ShotRecognition {

    private static final float RATE_1_3 = 1f / 3;
    private static final float RATE_1_9 = 1f / 9;
    private static final String TAG = "ShotRecognition";

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
            return keyPointCount >= 9 ? CATEGORY_PART : CATEGORY_PRODUCT;
        }
    }

    /** return  {@linkplain MetaInfo#SHOT_TYPE_NONE} means can't find shot type . need request 'subject-identification' */
    public static int getShotType(MediaPartItem item) {
        String shotType = item.imageMeta.getShotType();
        int oldType = SHOT_TYPE_NONE;
        if (!TextUtils.isEmpty(shotType)) {
            oldType = MetaInfo.getShotTypeFrom(shotType);
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
            return Math.max(oldType, bodyShotType);
        }
    }

    private static int getShotTypeByBody(MediaPartItem item) {
        float bodyArea = item.getBodyArea();
        float bodyRate = bodyArea / (item.imageMeta.getWidth() * item.imageMeta.getHeight());
        return getShopTypeByBody(bodyRate);
    }

    private static int getShotTypeByHighLight(MediaPartItem item) {
        List<IHighLightData> data;
        KeyValuePair<Integer, List<IHighLightData>> highLight = item.getHighLight();
        if (highLight != null) {
            data = highLight.getValue();
        } else {
            data = item.getHighLightData(item.getKeyFrameTime());
        }
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
        int shotType = MetaInfo.getShotTypeFrom(moduleData.getShotType());

        float heightRatio = hld.getLocation().height * 1f / item.imageMeta.getHeight();
        if(heightRatio <= RATE_1_9){
            shotType -= 4 ;
        }else if(heightRatio <= RATE_1_3){
            shotType -= 2;
        }
        if(shotType < SHOT_TYPE_BIG_LONG_SHORT){
            shotType = SHOT_TYPE_BIG_LONG_SHORT;
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
