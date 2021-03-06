package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;

import java.util.List;

/**
 * @author heaven7
 */
public class NormalStageFiller extends StageFiller {

    private final StageFiller matchFiller = new MatchStageFiller();
    private final StageFiller maxFiller = new MaxScoreStageFiller();
    private final StageFiller reuseFiller = new ReuseItemStageFiller();

    @Override
    protected void fillImpl(Context context, List<IPlaidInfo> newPlaids, List<MediaPartItem> items, GapManager.GapCallback callback) {

        System.out.println("start ====================== MatchStageFiller =====================");
        //match
        LeftFillInfo leftFillInfo = matchFiller.fill(context, newPlaids, items, callback);

        System.out.println("start ====================== MaxScoreStageFiller =====================");
        //max score
        if(!Predicates.isEmpty(leftFillInfo.getPlaids()) && !Predicates.isEmpty(leftFillInfo.getItems())){
            leftFillInfo = maxFiller.fill(context, leftFillInfo.getPlaids(),leftFillInfo.getItems(), callback);
        }
        System.out.println("start ====================== ReuseItemStageFiller =====================");
        //reuse item
        if(!Predicates.isEmpty(leftFillInfo.getPlaids())){
            reuseFiller.fill(context, leftFillInfo.getPlaids(), items, callback);
        }
        System.out.println("start ====================== end all StageFiller =====================");
    }
}
