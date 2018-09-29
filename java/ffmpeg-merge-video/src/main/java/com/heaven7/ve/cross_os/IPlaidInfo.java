package com.heaven7.ve.cross_os;

import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.gap.PlaidDelegate;

import java.util.List;

/**
 * @author heaven7
 */
public interface IPlaidInfo extends IPathTimeTraveller, PlaidDelegate {

    GapColorFilter getColorFilter();
    void setColorFilter(GapColorFilter filter);

    ITransitionInfo getTransitionInfo();
    void setTransitionInfo(ITransitionInfo info);

    List<IEffectInfo> getEffects();
    void setEffects(List<IEffectInfo> effects);

    IFilterInfo getFilter();
    void setFilter(IFilterInfo filter);

    int getEffectType();
    int getFilterType();
    int getTransitionType();
    int getColorFilterType();

    void clearEffects();
    void addColorFilter(GapColorFilter filter);
    void removeColorFilter(GapColorFilter filter);

    String getFilterString();
    boolean hasEffect();
    boolean isAir();
    void setAir(boolean air);

    //--------------------------------------------------------

    int getColorFilterWeight();
    void setColorFilterWeight(int weight);

    int getFilterWeight();
    void setFilterWeight(int weight);

    int getEffectWeight();
    void setEffectWeight(int weight);
    int getTransitionWeight();
    void setTransitionWeight(int weight);


}
