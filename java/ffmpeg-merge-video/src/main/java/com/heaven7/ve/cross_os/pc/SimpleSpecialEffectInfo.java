package com.heaven7.ve.cross_os.pc;

import com.heaven7.ve.SimpleCopyDelegate;
import com.heaven7.ve.cross_os.CopyHelper;
import com.heaven7.ve.cross_os.EffectsHelper;
import com.heaven7.ve.cross_os.ISpecialEffectInfo;

/**
 * @author heaven7
 */
/*public*/ class SimpleSpecialEffectInfo extends SimpleEffectInfo implements ISpecialEffectInfo {

    private int category;
    private float multiple;
    private float startSpeed;
    private float endSpeed;

    @Override
    public void setCategory(int category) {
        this.category = category;
    }
    @Override
    public int getCategory() {
        return category;
    }
    @Override
    public void setMultiple(float multiple) {
        this.multiple = multiple;
    }
    @Override
    public float getMultiple() {
        return multiple;
    }

    @Override
    public float getStartSpeed() {
        return startSpeed;
    }
    @Override
    public float getEndSpeed() {
        return endSpeed;
    }
    @Override
    public void setStartSpeed(float speed) {
        startSpeed = speed;
    }
    @Override
    public void setEndSpeed(float speed) {
        endSpeed = speed;
    }
    @Override
    public void setTypeFrom(String val) {
        EffectsHelper.setTypeForSpecialEffect(this, val);
    }
    @Override
    public void setFrom(SimpleCopyDelegate src) {
        super.setFrom(src);
        CopyHelper.copySpecialEffectInfo(this, src);
    }
}
