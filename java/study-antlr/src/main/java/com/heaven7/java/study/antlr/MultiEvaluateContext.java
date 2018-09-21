package com.heaven7.java.study.antlr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
/*public*/ class MultiEvaluateContext implements IEvaluateContext{

    private final List<IEvaluateContext> mList = new ArrayList<>(3);

    public void addEvaluateContext(IEvaluateContext context){
        mList.add(context);
    }
    public void removeEvaluateContext(IEvaluateContext context){
        mList.remove(context);
    }

    @Override
    public Object getObject(String alias) {
        for (IEvaluateContext context :mList){
            Object obj = context.getObject(alias);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }

    @Override
    public Class<?> getClass(String alisa) {
        for (IEvaluateContext context :mList){
            Class<?> obj = context.getClass(alisa);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }

    @Override
    public void putObject(String alias, Object obj) {
        mList.get(0).putObject(alias, obj);
    }
    @Override
    public void putClassname(String alias, String classname) {
        mList.get(0).putClassname(alias, classname);
    }
    @Override
    public void putClass(String alias, Class<?> clazz) {
        mList.get(0).putClass(alias, clazz);
    }

}
