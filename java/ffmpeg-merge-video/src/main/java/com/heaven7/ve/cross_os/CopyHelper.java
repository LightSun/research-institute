package com.heaven7.ve.cross_os;

import com.heaven7.java.visitor.util.Predicates;

/**
 * @author heaven7
 */
public class CopyHelper {

    public static void copyPlaidInfo(IPlaidInfo dst,  Object obj) {
        if(obj instanceof IPlaidInfo){
            IPlaidInfo src = (IPlaidInfo) obj;
            dst.setColorFilter(src.getColorFilter());
            dst.setFilter(src.getFilter());
            dst.setTransitionInfo(src.getTransitionInfo());
            if(!Predicates.isEmpty(src.getEffects())){
                dst.setEffects(src.getEffects());
            }
            dst.setColorFilterWeight(src.getColorFilterWeight());
            dst.setFilterWeight(src.getFilterWeight());
            dst.setTransitionWeight(src.getTransitionWeight());
            dst.setEffectWeight(src.getEffectWeight());
        }
    }

    public static void copyTimeTraveller(ITimeTraveller dst, Object obj) {
        if(obj instanceof ITimeTraveller) {
            ITimeTraveller src = (ITimeTraveller) obj;
            dst.setStartTime(src.getStartTime());
            dst.setEndTime(src.getEndTime());
            dst.setMaxDuration(src.getMaxDuration());
        }
    }

    public static void copyPathTimeTraveller(IPathTimeTraveller dst, Object obj) {
        if(obj instanceof IPathTimeTraveller){
            IPathTimeTraveller src = (IPathTimeTraveller) obj;
            dst.setType(src.getType());
            dst.setPath(src.getPath());
        }
    }

    public static void copyEffectInfo(IEffectInfo dst, Object obj) {
        if(obj instanceof IEffectInfo){
            IEffectInfo src = (IEffectInfo) obj;
            dst.setType(src.getType());
        }
    }
}
