package com.heaven7.ve.colorgap.impl;

import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicCutter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public class MusicCutterImpl implements MusicCutter {

    private static final String NAME = "e:/13.mp3";

    @Override
    public CutInfo[] cut(Context context, String[] musicPath) {
        CutInfo info = new CutInfo();
        int originDuration = 0;
        int duration = 281 * 1000; //00:04:41

        String path = NAME;

        //cut,
        List<CutInfo.PlaidInfo> infos = new ArrayList<>();
        Random r = new Random();
        originDuration = duration;
        if(duration > 60 * 1000){
            duration = 60 * 1000;
        }
        int left = duration;
        long lastEndTime = 0;
        while(left > 500){
            CutInfo.PlaidInfo pi = new CutInfo.PlaidInfo();
            long startTime = lastEndTime ;
            int val = r.nextInt(2000) + 1000;
            lastEndTime = startTime +  val;
            pi.setStartTime(CommonUtils.timeToFrame(startTime, TimeUnit.MILLISECONDS));
            pi.setEndTime(CommonUtils.timeToFrame(lastEndTime, TimeUnit.MILLISECONDS));
            pi.setPath(path);
            pi.setMaxDuration(CommonUtils.timeToFrame(originDuration, TimeUnit.MILLISECONDS));
            infos.add(pi);
            left -= val;
        }
        if(left > CommonUtils.frameToTime(3, TimeUnit.MILLISECONDS)){
            CutInfo.PlaidInfo pi = new CutInfo.PlaidInfo();
            pi.setStartTime(CommonUtils.timeToFrame(lastEndTime, TimeUnit.MILLISECONDS));
            pi.setEndTime(CommonUtils.timeToFrame(lastEndTime + left, TimeUnit.MILLISECONDS));
            pi.setPath(path);
            pi.setMaxDuration(CommonUtils.timeToFrame(originDuration, TimeUnit.MILLISECONDS));
            infos.add(pi);
        }
        info.setPlaidInfos(infos);
        return new CutInfo[]{info};
    }
}
