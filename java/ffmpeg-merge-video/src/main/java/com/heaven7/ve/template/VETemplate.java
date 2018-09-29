package com.heaven7.ve.template;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.cross_os.IPlaidInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * the video editor template
 * Created by heaven7 on 2018/3/23 0023.
 */
public class VETemplate {

   /* public static final int CHAPTER_FILL_TYPE_DEFAULT    = 1;
    public static final int CHAPTER_FILL_TYPE_SOTRY_LINE = 2;*/
    public static final String FILL_TYPE_STORY_LINE = "story_line";
    /** fill the chapter by filter then use other shots */
    public static final String FILL_TYPE_FILTER_THEN_OTHERS = "filter_then_others";
    /** fill the chapter by all-shots(include other chapters' shots) by max score */
    public static final String FILL_TYPE_ALL_SHOTS_MAX_SCORE = "all_shots_max_score";

    private List<LogicSentence> logicSentences;
    private String chapterFillType;
    private boolean releaseSentence;

    public boolean isReleaseSentence() {
        return releaseSentence;
    }
    public void setReleaseSentence(boolean releaseSentence) {
        this.releaseSentence = releaseSentence;
    }

    public String getChapterFillType() {
        return chapterFillType;
    }
    public void setChapterFillType(String chapterFillType) {
        this.chapterFillType = chapterFillType;
    }

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

        private List<IPlaidInfo> plaids;
        /** the weight */
        private int weight = -1 ;
        /** is the air shot or not .(this is used to join double sentence.) */
        private boolean air;

        private transient ProportionConfig mRuleConfig;
        private transient long percent;
        /** the extra weight which is compute bu merge */
        private int extraWeight;
        private String dir;
        private int shotCategoryFlags;

        public int getShotCategoryFlags() {
            return shotCategoryFlags;
        }
        public void setShotCategoryFlags(int shotCategoryFlags) {
            this.shotCategoryFlags = shotCategoryFlags;
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
                for (IPlaidInfo info : plaids) {
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

        public List<IPlaidInfo> getPlaids() {
            return plaids;
        }
        public void setPlaids(List<IPlaidInfo> plaids) {
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
                IPlaidInfo info = plaids.get(i);
                ProportionConfig.Rule effect = mRuleConfig.new Rule(i,size - 1, info.getEffectType());
                ProportionConfig.Rule filter = mRuleConfig.new Rule(i, size - 1, info.getFilterType());
                ProportionConfig.Rule transition = mRuleConfig.new Rule(i, size - 1,info.getTransitionType());
                ProportionConfig.Rule colorFilter = mRuleConfig.new Rule(i, size - 1, info.getColorFilterType());

                effectCount += info.hasEffect() ? 1 : 0;
                filterCount += info.getFilter() != null ? 1 : 0;
                transitionCount += info.getTransitionInfo() != null ? 1 : 0;
                colorFilterCount += info.getColorFilter() != null ? 1 : 0;
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
            for(IPlaidInfo info : plaids){
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
            for(IPlaidInfo info : plaids){
                info.clearEffects();
            }
        }
        public LogicSentence copy() {
            LogicSentence ls = new LogicSentence();
            ls.weight = this.weight;
            ls.air = this.air;
            if(getPlaids() != null){
                for(IPlaidInfo info : getPlaids()){
                    ls.addPlaidInfo((IPlaidInfo) info.copy());
                }
            }
            return ls;
        }
        public void addPlaidInfo(IPlaidInfo info) {
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
            for(IPlaidInfo info : plaids){
                info.addColorFilter(filter);
            }
        }
    }

}
