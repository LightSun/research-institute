package com.heaven7.ve.cross_os.pc;

import com.heaven7.java.base.anno.Platform;

import com.heaven7.ve.cross_os.*;

/**
 * @author heaven7
 */
@Platform
public class PCVEFactory extends VEFactory {

    @Override
    public IMediaResourceItem newMediaResourceItem() {
        return new SimpleMediaResourceItem();
    }
    @Override
    public ITimeTraveller newTimeTraveller() {
        return new SimpleTimeTraveller();
    }
    @Override
    public IPathTimeTraveller newPathTimeTraveller() {
        return new SimplePathTimeTraveller();
    }
    @Override
    public IEffectInfo newEffectInfo() {
        return new SimpleEffectInfo();
    }
    @Override
    public ISpecialEffectInfo newSpecialEffectInfo() {
        return new SimpleSpecialEffectInfo();
    }
    @Override
    public IFilterInfo newFilterInfo() {
        return new SimpleEffectInfo();
    }
    @Override
    public ITextureInfo newTextureInfo() {
        return new SimpleTextureInfo();
    }
    @Override
    public ITransitionInfo newTransitionInfo() {
        return new SimpleTransitionInfo();
    }
    @Override
    public IPlaidInfo newPlaidInfo() {
        return new SimplePlaidInfo();
    }
}
