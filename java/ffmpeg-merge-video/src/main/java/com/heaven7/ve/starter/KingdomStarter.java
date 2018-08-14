package com.heaven7.ve.starter;

import com.google.gson.GsonBuilder;
import com.heaven7.utils.Context;
import com.heaven7.utils.ReflectUtils;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class KingdomStarter implements IStarter {

    @Override
    public void init(Context context, Object param) {
        Kingdom.loadVocabulary(context, "table/vocabulary.csv");
        if(param != null){
            Kingdom kingdom = ReflectUtils.newInstance(param.toString());
            kingdom.asDefault();
        }else {
            Kingdom.fromKingdomData("table/kingdom_dress.json", new GsonBuilder()).asDefault();
        }
    }
}
