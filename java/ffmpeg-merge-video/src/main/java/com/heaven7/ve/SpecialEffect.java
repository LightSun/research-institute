package com.heaven7.ve;

/**
 * the special effect
 * Created by heaven7 on 2018/3/29 0029.
 */

public class SpecialEffect extends EffectInfo{

   /* public native void setCategory(int category);
    public native int getCategory();
    public native void setMultiple(float multiple);

    public native float getMultiple();*/

    @Override
    protected int getNativeType() {
        return NTYPE_SPECIAL_EFFECT;
    }

   /* @Override
    public void setFrom(TimeTraveller src) {
        super.setFrom(src);
        if(src instanceof SpecialEffect){
            setMultiple(((SpecialEffect) src).getMultiple());
        }
    }*/
}
