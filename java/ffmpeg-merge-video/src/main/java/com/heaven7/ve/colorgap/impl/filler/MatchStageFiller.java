package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;

import java.util.Arrays;
import java.util.List;

import static com.heaven7.ve.colorgap.VEGapUtils.filter;

/**
 * @author heaven7
 */
public class MatchStageFiller extends StageFiller {

    @Override
    public void fillImpl(Context context, List<IPlaidInfo> plaids, List<MediaPartItem> items, GapManager.GapCallback callback) {
        // List<CutInfo.PlaidInfo> notPopulatePlaids = new ArrayList<>();
        for(int i = 0 , size = plaids.size() ; i < size ; i++){
            IPlaidInfo info = plaids.get(i);
            List<MediaPartItem> result = filter(info, items);
            if(Predicates.isEmpty(result)){
                //notPopulatePlaids.add(info);
            }else{
                new GapManager(Arrays.asList(info), result).fill(callback, false, true);
            }
        }
    }

}
