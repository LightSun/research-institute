package com.heaven7.ve;

/**
 * 特效信息
 * Created by heaven7 on 2018/1/16 0016.
 */
public abstract class EffectInfo extends TimeTraveller{

    @Override
    protected TimeTravelEntityDelegate createImpl() {
        return new EffectInfoImpl();
    }
    public int getType(){
        return getDelegateAs(EffectEntityDelegate.class).getType();
    }
    public void setType(int type){
        getDelegateAs(EffectEntityDelegate.class).setType(type);
    }

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

    protected static class EffectInfoImpl extends TimeTravelDelegateImpl implements EffectEntityDelegate{
        private int type;
        @Override
        public int getType() {
            return type;
        }
        @Override
        public void setType(int type) {
            this.type = type;
        }
    }
}
