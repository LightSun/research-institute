package com.heaven7.ve.colorgap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.kingdom.ModuleData;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * @author heaven7
 */
public class ShotRecognition {

    /**
     * indicate shot is environment
     */
    public static final int CATEGORY_ENV = 1;
    /**
     * indicate shot is part
     */
    public static final int CATEGORY_PART = 2;
    /**
     * indicate shot is product
     */
    public static final int CATEGORY_PRODUCT = 3;

    private static final float RATE_1_3 = 1f / 3;
    private static final float RATE_1_9 = 1f / 9;
    private static final String TAG = "ShotRecognition";

    /***
     * recognize the shot category. such as {@linkplain #CATEGORY_ENV} and etc.
     * @param item the media part item
     * @return the shot category.
     */
    public static int recognizeShotCategory(MediaPartItem item) {
        int faceCount = item.imageMeta.getMainFaceCount();
        int keyPointCount = item.getKeyPointCount();
        if (faceCount > 0) {
            if (keyPointCount >= 9) {
                return CATEGORY_ENV;
            }
            return CATEGORY_PART;
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

        long duration = item.imageMeta.getDuration();
        int middleTime = (int) (duration / 2 / 1000);
        KeyValuePair<Integer, List<IHighLightData>> highLight = item.getHighLight();
        int time = highLight != null ? highLight.getKey() : middleTime;
        float bodyArea = item.getBodyArea(time);
        float bodyRate = bodyArea / (item.imageMeta.getWidth() * item.imageMeta.getHeight());
        int body_shotType = getShopTypeByBody(bodyRate);
        //trans shot_type and body type is none
        if(oldType == SHOT_TYPE_NONE && body_shotType == SHOT_TYPE_NONE){
            List<IHighLightData> data;
            if (highLight != null) {
                data = highLight.getValue();
            } else {
                data = item.getHighLightData(time);
            }
            //module shot type
            int shotType_module = SHOT_TYPE_NONE;
            if(!Predicates.isEmpty(data)){
                shotType_module = getShotTypeOfHighLight(item, data);
            }
            return shotType_module; //may be SHOT_TYPE_NONE
            //TODO 领域 主体。
        }else{
            return Math.max(oldType, body_shotType);
        }
    }

    private static int getShotTypeOfHighLight(MediaPartItem item, List<IHighLightData> data) {
        IHighLightData hld = VisitServices.from(data).pile(new PileVisitor<IHighLightData>() {
            @Override
            public IHighLightData visit(Object o, IHighLightData hd1, IHighLightData hd2) {
                return hd1.getLocation().getArea() > hd2.getLocation().getArea() ? hd1 : hd2;
            }
        });
        ModuleData moduleData = Kingdom.getDefault().getModuleData(hld.getName());
        if(moduleData == null){
            Logger.e(TAG , "getShotTypeOfHighLight", "no module data for name = " + hld.getName());
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
        } else if (CommonUtils.isInRange(area, 0.116f, 0.274f)) {
            return MetaInfo.SHOT_TYPE_MEDIUM_LONG_SHOT;
        } else if (CommonUtils.isInRange(area, 0.037f, 0.116f)) {
            return MetaInfo.SHOT_TYPE_LONG_SHORT;
        } else if (CommonUtils.isInRange(area, 0.0f, 0.037f)) {
            return MetaInfo.SHOT_TYPE_BIG_LONG_SHORT;
        }
        return SHOT_TYPE_NONE;
    }
}
