package com.heaven7.ve;

/**
 * the sub class must have the empty argument constructor.
 * Created by heaven7 on 2018/3/27 0027.
 */

public abstract class SimpleCopyDelegate implements Copyable<SimpleCopyDelegate> {

    @Override
    public final SimpleCopyDelegate copy() {
        SimpleCopyDelegate tt = create();
        tt.setFrom(this);
        return tt;
    }

    @Override
    public final SimpleCopyDelegate create() {
        try {
            return getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public abstract void setFrom(SimpleCopyDelegate sc);

}
