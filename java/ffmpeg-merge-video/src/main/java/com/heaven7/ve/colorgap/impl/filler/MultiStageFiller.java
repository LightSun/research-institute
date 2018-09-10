package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.gap.GapManager;

import java.util.Arrays;
import java.util.List;

/**
 * @author heaven7
 */
public class MultiStageFiller extends StageFiller {

    private final List<StageFiller> fillers;

    public MultiStageFiller(List<StageFiller> fillers) {
        this.fillers = fillers;
    }
    public MultiStageFiller(StageFiller...fillers){
        this(Arrays.asList(fillers));
    }

    @Override
    protected void fillImpl(Context context, List<CutInfo.PlaidInfo> newPlaids, List<MediaPartItem> items, GapManager.GapCallback callback) {
        LeftFillInfo info = null;
        for(StageFiller filler: fillers){
            if(info == null) {
                info = filler.fill(context, newPlaids, items, callback);
            }else{
                info = filler.fill(context, info.getPlaids(), info.getItems(), callback);
            }
            if(Predicates.isEmpty(info.getPlaids())){
                break;
            }
        }
    }
}
