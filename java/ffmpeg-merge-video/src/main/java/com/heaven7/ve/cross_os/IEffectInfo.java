package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public interface IEffectInfo extends ITimeTraveller{
    int getType();
    void setType(int type);

    void setTypeFrom(String val);

    //need
    boolean equals(Object o);
    int hashCode();
}
