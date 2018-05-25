package com.heaven7.ve.test;

import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicCutter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public class MusicCutterImpl2 implements MusicCutter {

    private static final String NAME = "e:/13.mp3";

    private final int count;

    public MusicCutterImpl2(int count) {
        this.count = count;
    }

    @Override
    public CutInfo[] cut(Context context, String[] musicPath) {
        int duration = 281 * 1000; //00:04:41
        CutInfo info = new CutInfo();
        //cut,
        List<CutInfo.PlaidInfo> infos = new ArrayList<>();
        //35个格子。每个2s
        int lastTime = 0; //second
        for(int i = 0 ; i < count ; i ++){
            long start = CommonUtils.timeToFrame(lastTime, TimeUnit.SECONDS);
            long end = start + CommonUtils.timeToFrame(2, TimeUnit.SECONDS);
            CutInfo.PlaidInfo pi = new CutInfo.PlaidInfo();
            pi.setPath(NAME);
            pi.setStartTime(start);
            pi.setEndTime(end);
            pi.setMaxDuration(CommonUtils.timeToFrame(duration, TimeUnit.MILLISECONDS));
            infos.add(pi);

            lastTime += 2;
        }
        info.setPlaidInfos(infos);
        return new CutInfo[]{info};
    }
}
