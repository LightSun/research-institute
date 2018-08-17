package com.heaven7.ve.starter;

import com.google.gson.GsonBuilder;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.utils.Context;
import com.heaven7.utils.ReflectUtils;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.kingdom.GelailiyaKingdom;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class KingdomStarter implements IStarter {

    public static final int TYPE_DRESS     = 3;
    public static final int TYPE_GELAILIYA = 2;
    public static final int TYPE_TARGET    = 1;
    private static final SparseArray<Kingdom> sKingdomMap = new SparseArray<>(3);

    public static Kingdom getKingdom(int type){
        return sKingdomMap.get(type);
    }

    @Override
    public void init(Context context, Object param) {
        Kingdom.loadVocabulary(context, "table/vocabulary.csv");
        if(param != null){
            Kingdom kingdom = ReflectUtils.newInstance(param.toString());
            sKingdomMap.put(TYPE_TARGET, kingdom);
        }
        //load all kingdom data.
        Kingdom kingdom = Kingdom.fromKingdomData("table/kingdom_dress.json", new GsonBuilder());
        sKingdomMap.put(TYPE_DRESS, kingdom);
        sKingdomMap.put(TYPE_GELAILIYA, new GelailiyaKingdom());
    }
}
