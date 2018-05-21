package com.heaven7.ve.colorgap;

import com.heaven7.utils.Arrays2;
import com.heaven7.ve.*;
import com.heaven7.ve.colorgap.filter.GroupFilter;
import com.heaven7.ve.gap.PlaidDelegate;
import com.heaven7.ve.template.ProportionConfig;

import java.util.List;

import static com.heaven7.ve.PathTimeTraveller.TYPE_AUDIO;

/**
 * 切点信息
 * Created by heaven7 on 2018/1/19 0019.
 */

public class CutInfo {

    private List<PlaidInfo> plaidInfos;

    public PlaidInfo getMaxDurationPlaid() {
        PlaidInfo info = plaidInfos.get(0);
        for (PlaidInfo pi : plaidInfos) {
            if (pi.getDuration() > info.getDuration()) {
                info = pi;
            }
        }
        return info;
    }

    public List<PlaidInfo> getPlaidInfos() {
        return plaidInfos;
    }

    public void setPlaidInfos(List<PlaidInfo> plaidInfos) {
        this.plaidInfos = plaidInfos;
    }

    @Override
    public String toString() {
        return "CutInfo{" +
                ", plaidInfos=" + plaidInfos +
                '}';
    }

    /**
     * plaid info
     */
    public static class PlaidInfo extends PathTimeTraveller implements PlaidDelegate {
        /**
         * may have some filters
         */
        private GapColorFilter colorFilter;
        private transient boolean isHold;

        private List<EffectInfo> effects;
        private FilterInfo filter;
        /** for transition:  this plaid as the right plaid. ( plaid_left : plaid_right ) */
        private TransitionInfo transitionInfo;

        private int colorFilterWeight;
        private int filterWeight;
        private int effectWeight;
        private int transitionWeight;
        /** indicate will put a air-shot */
        private boolean air;

        public PlaidInfo() {
            setType(TYPE_AUDIO);
        }

        public void setGapColorFilter(GapColorFilter filter) {
            this.colorFilter = filter;
        }

        public GapColorFilter getGapColorFilter() {
            return colorFilter;
        }
        public List<EffectInfo> getEffects() {
            return effects;
        }
        public void setEffects(List<EffectInfo> effects) {
            this.effects = effects;
        }
        public FilterInfo getFilter() {
            return filter;
        }

        public void setFilter(FilterInfo filter) {
            this.filter = filter;
        }
        public TransitionInfo getTransitionInfo() {
            return transitionInfo;
        }

        public void setTransitionInfo(TransitionInfo transitionInfo) {
            this.transitionInfo = transitionInfo;
        }

        @Override
        public void setFrom(TimeTraveller src) {
            super.setFrom(src);
            if(src instanceof PlaidInfo){
                PlaidInfo src_pi = (PlaidInfo) src;
                this.colorFilter = src_pi.getGapColorFilter();
                this.filter = src_pi.getFilter();
                this.transitionInfo = src_pi.getTransitionInfo();
                if(src_pi.getEffects() != null){
                    this.effects = Arrays2.copy(src_pi.getEffects());
                }
                this.colorFilterWeight = src_pi.colorFilterWeight;
                this.filterWeight = src_pi.filterWeight;
                this.effectWeight = src_pi.effectWeight;
                this.transitionWeight = src_pi.transitionWeight;
                this.air = src_pi.air;
            }
        }

        @Override
        public boolean isHold() {
            return isHold;
        }

        @Override
        public void setHold(boolean hold) {
            isHold = hold;
        }

        @Override
        public float computeScore(GapColorFilter.GapColorCondition condition) {
            GapColorFilter filter = getGapColorFilter();
            if(filter == null){
                return 0;
            }
            return filter.computeScoreWithWeight(condition);
        }

        public boolean hasEffect() {
            return effects != null && effects.size() > 0;
        }
        public int getColorFilterWeight() {
            return colorFilterWeight;
        }
        public void setColorFilterWeight(int colorFilterWeight) {
            this.colorFilterWeight = colorFilterWeight;
        }
        public int getFilterWeight() {
            return filterWeight;
        }
        public void setFilterWeight(int filterWeight) {
            this.filterWeight = filterWeight;
        }
        public int getEffectWeight() {
            return effectWeight;
        }
        public void setEffectWeight(int effectWeight) {
            this.effectWeight = effectWeight;
        }
        public int getTransitionWeight() {
            return transitionWeight;
        }
        public void setTransitionWeight(int transitionWeight) {
            this.transitionWeight = transitionWeight;
        }
        public int getEffectType() {
            List<EffectInfo> effects = getEffects();
            if(effects != null){
                if(effects.size() == 0){
                    return effects.get(0).getType();
                }
                return ProportionConfig.TYPE_COMPLEX;
            }
            return ProportionConfig.TYPE_NONE;
        }
        public int getFilterType() {
            if(getFilter() != null){
                return getFilter().getType();
            }
            return ProportionConfig.TYPE_NONE;
        }
        public int getTransitionType() {
            if(getTransitionInfo() != null){
                return getTransitionInfo().getType();
            }
            return ProportionConfig.TYPE_NONE;
        }
        public int getColorFilterType() {
            GapColorFilter colorFilter = getGapColorFilter();
            if(colorFilter != null){
                if(colorFilter instanceof GroupFilter){
                    GroupFilter gf = (GroupFilter) colorFilter;
                    return gf.getTotalFlags();
                }
                return colorFilter.getFlag();
            }
            return ProportionConfig.TYPE_NONE;
        }

        public void clearEffects() {
            colorFilter = null;
            if(effects != null) {
                effects.clear();
            }
            filter = null;
            transitionInfo = null;
        }

        public void addColorFilter(GapColorFilter filter) {
            if(colorFilter == null){
                colorFilter = new GroupFilter();
            }
            ((GroupFilter)colorFilter).addGapColorFilter(filter);
        }

        public void removeColorFilter(GapColorFilter filter) {
            if(colorFilter != null && colorFilter instanceof GroupFilter){
                ((GroupFilter) colorFilter).removeGapColorFilter(filter);
            }
        }

        public void setAir(boolean air) {
           // plaid.addColorFilter(MediaDirFilter.AIR_SHOT);
            this.air = air;
        }
        public boolean isAir() {
            return air;
        }
    }

}
