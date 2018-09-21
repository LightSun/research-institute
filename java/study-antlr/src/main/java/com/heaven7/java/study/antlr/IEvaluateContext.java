package com.heaven7.java.study.antlr;

/**
 * @author heaven7
 */
public interface IEvaluateContext {

    Object getObject(String alias);

    Class<?> getClass(String alisa);

    void putObject(String alias, Object obj);

    void putClassname(String alias, String classname);

    void putClass(String alias, Class<?> clazz);
}
