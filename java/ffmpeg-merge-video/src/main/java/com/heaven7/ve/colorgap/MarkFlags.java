package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.cross_os.ITimeTraveller;
import com.heaven7.ve.template.EffectData;
import com.heaven7.ve.utils.EffectsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class MarkFlags {

    public static final int TYPE_TEXTURE     = 1;
    public static final int TYPE_TRANSITION  = 2;
    public static final int TYPE_FILTER      = 3;
    public static final int TYPE_SPECIAL_EFFECT = 4;

    private static final int[] TYPES = {TYPE_TEXTURE, TYPE_TRANSITION, TYPE_FILTER, TYPE_SPECIAL_EFFECT};

    public static final int FLAG_INDEX    = 0x0001;
    public static final int FLAG_CHAPTER  = 0x0002;
    public static final int FLAG_LEVEL    = 0x0004;

    // key is type,
    private SparseArray<List<EffectItemDelegate>> mEffectMap;
    private SparseArray<List<ITimeTraveller>> mAppliedEffectMap;

    public boolean hasFlags(int type, int flag){
        if(mEffectMap == null){
            return false;
        }
        List<EffectItemDelegate> delegates = mEffectMap.get(type);
        if(Predicates.isEmpty(delegates)){
            return false;
        }
        return VisitServices.from(delegates).filter(new PredicateVisitor<EffectItemDelegate>() {
            @Override
            public Boolean visit(EffectItemDelegate delegate, Object param) {
                return delegate.flag == flag;
            }
        }).size() > 0 ;
       // return Flags.hasFlags(mFlags, flag);
    }

    public void addEffectItemDelegate(int type, EffectItemDelegate delegate){
        if(mEffectMap == null){
            mEffectMap = new SparseArray<>();
        }
        List<EffectItemDelegate> delegates = mEffectMap.get(type);
        if(delegates == null){
            delegates = new ArrayList<>();
            mEffectMap.put(type, delegates);
        }
        delegates.add(delegate);
    }

    public void clearEffectItemDelegates(){
        if(mEffectMap != null){
            mEffectMap.clear();
        }
    }
    public SparseArray<List<ITimeTraveller>> getAppliedEffectMap(){
        return mAppliedEffectMap;
    }
    public List<EffectItemDelegate> getEffectItemDelegates(int type){
        if(mEffectMap == null){
            return null;
        }
        return mEffectMap.get(type);
    }

    /**
     * apply effects by
     * @param context the context
     * @param tt the time traveller.
     * @param nextTt the next time traveller.
     */
    public void applyEffects(ColorGapContext context, ITimeTraveller tt, ITimeTraveller nextTt) {
        if(mEffectMap == null){
            return;
        }
        EffectsHelper helper = new EffectsHelper();
        VisitServices.from(ArrayUtils.toList(TYPES)).fire(new FireVisitor<Integer>() {
            @Override
            public Boolean visit(Integer type, Object param) {
                List<EffectItemDelegate> delegates = mEffectMap.get(type);
                if(!Predicates.isEmpty(delegates)){
                    helper.transform(context, delegates, type , tt, nextTt);
                }
                return null;
            }
        });
        mAppliedEffectMap = helper.getAppliedEffectMap();
    }

    public static String getFlagString(int flag){
        switch (flag){
            case FLAG_INDEX:
                return "FLAG_INDEX";
            case FLAG_CHAPTER:
                return "FLAG_CHAPTER";
            case FLAG_LEVEL:
                return "FLAG_LEVEL";
        }
        throw new RuntimeException("unsupport flag = " + flag);
    }

    public static class EffectItemDelegate{
        public final EffectData.Item item;
        public final int flag;
        public EffectItemDelegate(EffectData.Item item, int flag) {
            this.item = item;
            this.flag = flag;
        }

        @Override
        public String toString() {
            return "EffectItemDelegate{" +
                    "item=" + item +
                    ", flag=" + getFlagString(flag) +
                    '}';
        }
    }
}
