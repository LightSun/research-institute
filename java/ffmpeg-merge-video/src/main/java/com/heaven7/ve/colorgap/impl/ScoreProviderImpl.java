package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.VideoHighLightManager;
import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.kingdom.ModuleData;

/**
 * @author heaven7
 */
public class ScoreProviderImpl implements VideoHighLightManager.ScoreProvider {

    @Override
    public float getCommonScore(Object context, IHighLightData hld) {
        Kingdom kingdom = VEGapUtils.getKingdom(context);
        ModuleData moduleData = kingdom.getModuleData(hld.getName());
        if(moduleData == null){
            return 0f;
        }
        return moduleData.getScore();
    }
}
