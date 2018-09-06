package com.heaven7.ve.utils;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.*;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.MarkFlags;
import com.heaven7.ve.template.EffectData;

import java.util.List;
import java.util.Random;

/**
 * @author heaven7
 */
public class EffectsHelper {

    private static final Random RANDOM = new Random();
    private final SparseArray<List<TimeTraveller>> mAppliedEffectMap = new SparseArray<>();

    public void transform(ColorGapContext context, List<MarkFlags.EffectItemDelegate> delegates, int type,
                          TimeTraveller tt, TimeTraveller nextTt){
        List<TimeTraveller> list = VisitServices.from(delegates).map(
                new ResultVisitor<MarkFlags.EffectItemDelegate, TimeTraveller>() {
                    @Override
                    public TimeTraveller visit(MarkFlags.EffectItemDelegate delegate, Object param) {
                        return transformItemImpl(context, type,delegate.item , tt, nextTt);
                    }
                }).getAsList();
        if(!Predicates.isEmpty(list)) {
            mAppliedEffectMap.put(type, list);
        }
    }

    private static TimeTraveller transformItemImpl(ColorGapContext context, int type, EffectData.Item item,
                                                   TimeTraveller tt, TimeTraveller nextTt){
        if (item.getPercentage() > 0f) {
            int value = (int) (item.getPercentage() * 100);
            if (randomTrue(100, value)) {
                if (!Predicates.isEmpty(item.getValues())) {
                    String val = randomValue(item.getValues());
                    return createItem(context, type, item, val , tt, nextTt);
                } else if (item.getValue() != null) {
                    return createItem(context, type, item, item.getValue(), tt, nextTt);
                }
            }
        } else if (item.getValue() != null) {
            return createItem(context, type, item, item.getValue(), tt, nextTt);
        }
        return null;
    }

    private static TimeTraveller createItem(ColorGapContext context, int type, EffectData.Item item, String val,
                                            TimeTraveller tt, TimeTraveller nextTt) {
        switch (type){
            case MarkFlags.TYPE_TEXTURE:
                TextureInfo info = new TextureInfo();
                info.setPath(item.getType() + "/" + val);
                info.setStartTime(tt.getStartTime());
                info.setEndTime(tt.getEndTime());
                return info;


            case MarkFlags.TYPE_FILTER:
                FilterInfo info1 = new FilterInfo();
                info1.setTypeFrom(val);
                info1.setStartTime(tt.getStartTime());
                info1.setEndTime(tt.getEndTime());
                return info1;

            case MarkFlags.TYPE_SPECIAL_EFFECT:
                SpecialEffect info2 = new SpecialEffect();
                info2.setTypeFrom(val);
                info2.setStartTime(tt.getStartTime());
                info2.setEndTime(tt.getEndTime());
                return info2;

            case MarkFlags.TYPE_TRANSITION:
                TransitionInfo info3 = new TransitionInfo();
                info3.setTypeFrom(val);
                long duration = context.getInitializeParam().getTransitionDelegate().getDuration(info3.getType());
                info3.setStartTime(tt.getEndTime() - duration /2);
                info3.setEndTime(nextTt.getStartTime() + duration/2);
                return info3;

        }
        return null;
    }

    public SparseArray<List<TimeTraveller>> getAppliedEffectMap() {
        return mAppliedEffectMap;
    }

    private static String randomValue(List<String> strs) {
        if (Predicates.isEmpty(strs)) {
            return null;
        }
        if (strs.size() == 1) {
            return strs.get(0);
        }
        int index = RANDOM.nextInt(strs.size());
        return strs.get(index);
    }

    private static boolean randomTrue(int max, int val) {
        return RANDOM.nextInt(max) + 1 <= val;
    }


}
