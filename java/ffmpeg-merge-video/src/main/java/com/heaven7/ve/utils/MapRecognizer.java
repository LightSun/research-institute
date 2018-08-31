package com.heaven7.ve.utils;

import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.Map;

/**
 * the map recognizer.
 * @author heaven7
 */
public abstract class MapRecognizer<V> {

    public static final String KEY_DEFAULT  = "default";
    public static final String SPLIT_SYMBOL = "-";

    private final Map<String, V> mMap;

    public MapRecognizer(Map<String, V> mMap) {
        this.mMap = mMap;
        final String splitSymbol = getSplitSymbol();
        if(splitSymbol != null) {
            VisitServices.from(mMap).fire(new MapFireVisitor<String, V>() {
                @Override
                public Boolean visit(KeyValuePair<String, V> pair, Object param) {
                    String key = pair.getKey(); //"3-5"
                    if (key.contains(splitSymbol) && shouldSplit(key)) {
                        for(String str : key.split(splitSymbol)){
                            mMap.put(str, pair.getValue());
                        }
                    }
                    return null;
                }
            });
        }
    }

    /**
     * get the value by target key
     * @param key the key
     * @return the value . if not found return the default value by {@linkplain #getDefaultValue()}.
     */
    public V getValue(String key){
        V v = mMap.get(key);
        return v != null ? v : getDefaultValue();
    }

    /**
     * get the default value.
     * @return the default value.
     */
    public V getDefaultValue(){
        return mMap.get(KEY_DEFAULT);
    }

    /**
     * should split the target key
     * @param key the key.
     * @return true if should split the key
     */
    protected boolean shouldSplit(String key){
        return true;
    }

    /**
     * get the split symbol. default is {@linkplain #SPLIT_SYMBOL}
     * @return the split symbol
     */
    protected String getSplitSymbol(){
        return SPLIT_SYMBOL;
    }
}
