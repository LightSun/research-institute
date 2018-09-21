package com.heaven7.java.study.antlr;

import java.util.Map;

/**
 * @author heaven7
 */
public class EvaluateContext implements IEvaluateContext {

    private final Map<String, Object> map;

    public EvaluateContext(Map<String, Object> map) {
        this.map = map;
    }
    public void putObject(String alias, Object obj) {
        map.put(alias, obj);
    }
    public Object getObject(String alias) {
        return map.get(alias);
    }
    public void putClassname(String alias, String cn){
        try {
            map.put(alias, Class.forName(cn));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void putClass(String alias, Class<?> clazz){
        map.put(alias, clazz);
    }

    public Class<?> getClass(String alisa){
        Object value = map.get(alisa);
        if(value instanceof Class){
            return (Class<?>) value;
        }
        throw new EvaluateException("type mismatched");
    }

}
