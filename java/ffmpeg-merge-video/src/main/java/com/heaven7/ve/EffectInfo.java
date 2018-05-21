package com.heaven7.ve;

/**
 * 特效信息
 * Created by heaven7 on 2018/1/16 0016.
 */
public abstract class EffectInfo extends TimeTraveller{

   // protected int type; //类型。由c/c++底层提供
    public static final int CATEGORY_NONE = 0;
    public static final int CATEGORY_SPEED = 1;
    public static final int CATEGORY_SCALE = 2;

    public native int getType();
    public native void setType(int type);

    @Override
    public void setFrom(TimeTraveller src) {
        super.setFrom(src);
        if(src instanceof EffectInfo){
            setType(((EffectInfo) src).getType());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EffectInfo that = (EffectInfo) o;

        return getType() == that.getType();
    }
    @Override
    public int hashCode() {
        return getType();
    }
}
