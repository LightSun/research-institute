package com.heaven7.ve.starter;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MusicCutter;

/**
 * @author heaven7
 */
public class SimpleMusicCutter implements MusicCutter{

    private final int duration;
    private final String cuts;

    public SimpleMusicCutter(String cuts, int duration) {
        this.cuts = cuts;
        this.duration = duration;
    }

    @Override
    public CutInfo[] cut(Context context, String[] strs) {
        if(strs.length != 1){
            throw new UnsupportedOperationException();
        }
        CutInfo info = new CutInfo();
        info.setPlaidInfos(MusicCutStarter.convertCuts(strs[0], cuts, duration));
        return new CutInfo[]{info};
    }
}
