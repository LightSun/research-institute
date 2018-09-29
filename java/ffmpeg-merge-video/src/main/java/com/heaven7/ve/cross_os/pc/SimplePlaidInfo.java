package com.heaven7.ve.cross_os.pc;

import com.heaven7.java.visitor.util.Predicates;
import com.heaven7.ve.SimpleCopyDelegate;
import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfoUtils;
import com.heaven7.ve.colorgap.filter.GroupFilter;
import com.heaven7.ve.cross_os.*;
import com.heaven7.ve.template.ProportionConfig;
import com.heaven7.ve.utils.Flags;

import java.util.List;

/**
 * @author heaven7
 */
/*public*/ class SimplePlaidInfo extends SimpleCopyDelegate implements IPathTimeTraveller, IPlaidInfo {

    private final IPathTimeTraveller mPPT;
    /**
     * may have some filters
     */
    private GapColorFilter colorFilter;
    private transient boolean isHold;

    private List<IEffectInfo> effects;
    private IFilterInfo filter;
    /** for transition:  this plaid as the right plaid. ( plaid_left : plaid_right ) */
    private ITransitionInfo ITransitionInfo;

    private int colorFilterWeight;
    private int filterWeight;
    private int effectWeight;
    private int transitionWeight;
    private boolean air;

    public SimplePlaidInfo() {
        this(VEFactory.getDefault().newPathTimeTraveller());
    }

    private SimplePlaidInfo(IPathTimeTraveller ptt) {
        this.mPPT = ptt;
        setType(TYPE_AUDIO);
    }

    @Override
    public void setColorFilter(GapColorFilter filter) {
        this.colorFilter = filter;
    }
    @Override
    public GapColorFilter getColorFilter() {
        return colorFilter;
    }
    @Override
    public List<IEffectInfo> getEffects() {
        return effects;
    }
    @Override
    public void setEffects(List<IEffectInfo> effects) {
        this.effects = effects;
    }
    @Override
    public IFilterInfo getFilter() {
        return filter;
    }
    @Override
    public void setFilter(IFilterInfo filter) {
        this.filter = filter;
    }
    @Override
    public ITransitionInfo getTransitionInfo() {
        return ITransitionInfo;
    }
    @Override
    public void setTransitionInfo(ITransitionInfo ITransitionInfo) {
        this.ITransitionInfo = ITransitionInfo;
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
        GapColorFilter filter = getColorFilter();
        if(filter == null){
            return 0;
        }
        return filter.computeScoreWithWeight(condition);
    }

    @Override
    public void setAir(boolean air) {
        this.air = air;
    }
    @Override
    public boolean isAir() {
        return air;
    }
    @Override
    public boolean hasEffect() {
        return effects != null && effects.size() > 0;
    }
    @Override
    public int getColorFilterWeight() {
        return colorFilterWeight;
    }
    @Override
    public void setColorFilterWeight(int colorFilterWeight) {
        this.colorFilterWeight = colorFilterWeight;
    }
    @Override
    public int getFilterWeight() {
        return filterWeight;
    }
    @Override
    public void setFilterWeight(int filterWeight) {
        this.filterWeight = filterWeight;
    }
    @Override
    public int getEffectWeight() {
        return effectWeight;
    }
    @Override
    public void setEffectWeight(int effectWeight) {
        this.effectWeight = effectWeight;
    }
    @Override
    public int getTransitionWeight() {
        return transitionWeight;
    }
    @Override
    public void setTransitionWeight(int transitionWeight) {
        this.transitionWeight = transitionWeight;
    }
    @Override
    public int getEffectType() {
        List<IEffectInfo> effects = getEffects();
        if(!Predicates.isEmpty(effects)){
            if(effects.size() == 1){
                return effects.get(0).getType();
            }
            return ProportionConfig.TYPE_COMPLEX;
        }
        return ProportionConfig.TYPE_NONE;
    }
    @Override
    public int getFilterType() {
        if(getFilter() != null){
            return getFilter().getType();
        }
        return ProportionConfig.TYPE_NONE;
    }
    @Override
    public int getTransitionType() {
        if(getTransitionInfo() != null){
            return getTransitionInfo().getType();
        }
        return ProportionConfig.TYPE_NONE;
    }
    @Override
    public int getColorFilterType() {
        GapColorFilter colorFilter = getColorFilter();
        if(colorFilter != null){
            if(colorFilter instanceof GroupFilter){
                GroupFilter gf = (GroupFilter) colorFilter;
                return gf.getTotalFlags();
            }
            return colorFilter.getFlag();
        }
        return ProportionConfig.TYPE_NONE;
    }
    @Override
    public void clearEffects() {
        colorFilter = null;
        if(effects != null) {
            effects.clear();
        }
        filter = null;
        ITransitionInfo = null;
    }
    @Override
    public void addColorFilter(GapColorFilter filter) {
        if(colorFilter == null){
            colorFilter = new GroupFilter();
        }
        ((GroupFilter)colorFilter).addGapColorFilter(filter);
    }
    @Override
    public void removeColorFilter(GapColorFilter filter) {
        if(colorFilter != null && colorFilter instanceof GroupFilter){
            ((GroupFilter) colorFilter).removeGapColorFilter(filter);
        }
    }
    @Override
    public String getFilterString(){
        if(colorFilter == null){
            return null;
        }
        if(colorFilter instanceof GroupFilter){
            int flags = ((GroupFilter) colorFilter).getTotalFlags();
            return Flags.getFlagsString(flags, MetaInfoUtils::getFlagString);
        }
        return MetaInfoUtils.getFlagString(colorFilter.getFlag());
    }

    //=========================== wrap ======================================
    @Override
    public String getPath() {
        return mPPT.getPath();
    }
    @Override
    public void setPath(String path) {
        mPPT.setPath(path);
    }
    @Override
    public int getType() {
        return mPPT.getType();
    }
    @Override
    public void setType(int type) {
        mPPT.setType(type);
    }
    @Override
    public void setFrom(SimpleCopyDelegate scd) {
        setFrom((ITimeTraveller)scd);
    }
    @Override
    public long getDuration() {
        return mPPT.getDuration();
    }
    @Override
    public long increaseStartTime(long delta) {
        return mPPT.increaseStartTime(delta);
    }
    @Override
    public long decreaseStartTime(long delta) {
        return mPPT.decreaseStartTime(delta);
    }
    @Override
    public long increaseEndTime(long delta) {
        return mPPT.increaseEndTime(delta);
    }
    @Override
    public long decreaseEndTime(long delta) {
        return mPPT.decreaseEndTime(delta);
    }
    @Override
    public long clampEndTime() {
        return mPPT.clampEndTime();
    }
    @Override
    public void adjustByLimit() {
        mPPT.adjustByLimit();
    }
    @Override
    public void setFrom(ITimeTraveller src) {
        CopyHelper.copyTimeTraveller(this, src);
        CopyHelper.copyPathTimeTraveller(this, src);
        CopyHelper.copyPlaidInfo(this, src);
    }
    @Override
    public String toString2() {
        return mPPT.toString2();
    }
    @Override
    public void adjustTime(long focusTime, long expectDuration) {
        mPPT.adjustTime(focusTime, expectDuration);
    }
    @Override
    public void adjustTimeAsCenter(long expectDuration) {
        mPPT.adjustTimeAsCenter(expectDuration);
    }
    @Override
    public long getStartTime() {
        return mPPT.getStartTime();
    }
    @Override
    public void setStartTime(long startTime) {
        mPPT.setStartTime(startTime);
    }

    @Override
    public long getEndTime() {
        return mPPT.getEndTime();
    }
    @Override
    public void setEndTime(long endTime) {
        mPPT.setEndTime(endTime);
    }

    @Override
    public void setMaxDuration(long maxDuration) {
        mPPT.setMaxDuration(maxDuration);
    }
    @Override
    public long getMaxDuration() {
        return mPPT.getMaxDuration();
    }
}
