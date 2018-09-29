package com.heaven7.ve.colorgap;

import com.heaven7.ve.cross_os.IPlaidInfo;

import java.util.List;

/**
 * 切点信息
 * Created by heaven7 on 2018/1/19 0019.
 */

public class CutInfo {

    private List<IPlaidInfo> plaidInfos;

    public IPlaidInfo getMaxDurationPlaid() {
        IPlaidInfo info = plaidInfos.get(0);
        for (IPlaidInfo pi : plaidInfos) {
            if (pi.getDuration() > info.getDuration()) {
                info = pi;
            }
        }
        return info;
    }

    public List<IPlaidInfo> getPlaidInfos() {
        return plaidInfos;
    }

    public void setPlaidInfos(List<IPlaidInfo> plaidInfos) {
        this.plaidInfos = plaidInfos;
    }

    @Override
    public String toString() {
        return "CutInfo{" +
                ", plaidInfos=" + plaidInfos +
                '}';
    }

}
