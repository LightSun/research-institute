package com.heaven7.ve.cross_os.pc;

import com.heaven7.ve.SimpleCopyDelegate;
import com.heaven7.ve.cross_os.*;

import java.util.Objects;

/**
 * @author heaven7
 */
/*public*/ class SimpleEffectInfo extends SimpleTimeTraveller implements IEffectInfo, IFilterInfo{

    private int type;
    @Override
    public int getType() {
        return type;
    }
    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void setFrom(SimpleCopyDelegate src) {
        super.setFrom(src);
        CopyHelper.copyEffectInfo(this, src);
    }

    @Override
    public void setTypeFrom(String val) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleEffectInfo that = (SimpleEffectInfo) o;
        return type == that.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
