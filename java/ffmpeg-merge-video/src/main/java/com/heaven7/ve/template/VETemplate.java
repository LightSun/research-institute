package com.heaven7.ve.template;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the video editor template
 * Created by heaven7 on 2018/3/23 0023.
 */
public class VETemplate {

    private List<LogicSentence> logicSentences;

    public List<LogicSentence> getLogicSentences() {
        return logicSentences;
    }
    public void setLogicSentences(List<LogicSentence> logicSentences) {
        this.logicSentences = logicSentences;
    }

    /** compute the percent of logic sentence */
    public void computePercent(){
        if(logicSentences != null){
            int totalPlaidCount = getTotalPlaidCount();
            for(LogicSentence ls : logicSentences){
                ls.setPercent(ls.getPlaidCount() / totalPlaidCount);
            }
        }
    }
    public void addLogicSentence(LogicSentence ls) {
        if(logicSentences == null){
            logicSentences = new ArrayList<>();
        }
        logicSentences.add(ls);
    }

    public void setTotalScale(float scale) {
        for(LogicSentence ls : logicSentences){
            ls.setScale(scale);
        }
    }
    public int getTotalPlaidCount(){
        int plaidCount = 0;
        for(LogicSentence ls : logicSentences){
            plaidCount += ls.getPlaids().size();
        }
        return plaidCount;
    }

    public VETemplate copy() {
        VETemplate template = new VETemplate();
        for(LogicSentence ls : logicSentences){
            template.addLogicSentence(ls.copy());
        }
        return template;
    }

    public static class LogicSentence{

        private List<CutInfo.PlaidInfo> plaids;
        /** the weight */
        private int weight = -1 ;
        /** is the air shot or not .(this is used to join double sentence.) */
        private boolean air;

        private transient ProportionConfig mRuleConfig;
        private transient long percent;
        /** the extra weight which is compute bu merge */
        private int extraWeight;
        private String dir;
        /** the shot-category */
        private int shotCategory;

        public int getShotCategory() {
            return shotCategory;
        }
        public void setShotCategory(int shotCategory) {
            this.shotCategory = shotCategory;
        }

        public String getDir() {
            return dir;
        }
        public void setDir(String dir) {
            this.dir = dir;
        }

        public boolean isAir() {
            return air;
        }
        public void setAir(boolean air) {
            this.air = air;
            setDir(air ? MetaInfo.DIR_EMPTY : null);
            if(!Predicates.isEmpty(plaids)) {
                for (CutInfo.PlaidInfo info : plaids) {
                    info.setAir(air);
                }
            }
        }

        /** get the base weight*/
        public int getWeight() {
            if(weight == -1){
                //如果没有给定权重，根据格子个数来。
                weight = plaids.size();
            }
            return weight;
        }
        /** get the actual weight which may have extra weight. */
        public int getActualWeight(){
            return getWeight() + extraWeight;
        }
        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<CutInfo.PlaidInfo> getPlaids() {
            return plaids;
        }
        public void setPlaids(List<CutInfo.PlaidInfo> plaids) {
            this.plaids = plaids;
        }
        /*public*/ void setScale(float scale) {
            mRuleConfig = new ProportionConfig(scale);
            //1个格子当成一个整体(可能有多个特效)。有特效则1.否则0
            int effectCount = 0;
            int filterCount = 0;
            int transitionCount = 0;
            int colorFilterCount = 0;
            int size = plaids.size();
            for(int i = 0; i < size ; i ++ ){
                CutInfo.PlaidInfo info = plaids.get(i);
                ProportionConfig.Rule effect = mRuleConfig.new Rule(i,size - 1, info.getEffectType());
                ProportionConfig.Rule filter = mRuleConfig.new Rule(i, size - 1, info.getFilterType());
                ProportionConfig.Rule transition = mRuleConfig.new Rule(i, size - 1,info.getTransitionType());
                ProportionConfig.Rule colorFilter = mRuleConfig.new Rule(i, size - 1, info.getColorFilterType());

                effectCount += info.hasEffect() ? 1 : 0;
                filterCount += info.getFilter() != null ? 1 : 0;
                transitionCount += info.getTransitionInfo() != null ? 1 : 0;
                colorFilterCount += info.getGapColorFilter() != null ? 1 : 0;
                mRuleConfig.addEffectRule(effect);
                mRuleConfig.addFilterRule(filter);
                mRuleConfig.addColorFilterRule(colorFilter);
                mRuleConfig.addTransitionRule(transition);
            }
            mRuleConfig.effectCount = effectCount;
            mRuleConfig.filterCount = filterCount;
            mRuleConfig.transitionCount = transitionCount;
            mRuleConfig.colorFilterCount = colorFilterCount;
        }

        public ProportionConfig getProportionConfig(){
            return mRuleConfig;
        }

        public long getDuration() {
            long duration = 0;
            for(CutInfo.PlaidInfo info : plaids){
                duration += info.getDuration();
            }
            return duration;
        }
        public int getPlaidCount(){
            return getPlaids().size();
        }
        public void setPercent(long percent) {
            this.percent = percent;
        }
        public long getPercent() {
            return percent;
        }
        public void clearEffects() {
            for(CutInfo.PlaidInfo info : plaids){
                info.clearEffects();
            }
        }
        public LogicSentence copy() {
            LogicSentence ls = new LogicSentence();
            ls.weight = this.weight;
            ls.air = this.air;
            if(getPlaids() != null){
                for(CutInfo.PlaidInfo info : getPlaids()){
                    ls.addPlaidInfo((CutInfo.PlaidInfo) info.copy());
                }
            }
            return ls;
        }
        public void addPlaidInfo(CutInfo.PlaidInfo info) {
            if(plaids == null){
                plaids = new ArrayList<>();
            }
            plaids.add(info);
        }

        public void addAdditionalWeight(int weight) {
            this.extraWeight += weight;
        }

        /** tint the all flags by target filter. */
        public void addColorFilter(GapColorFilter filter) {
            for(CutInfo.PlaidInfo info : plaids){
                info.addColorFilter(filter);
            }
        }
    }

}
