package test.provide.framework;

/**
 * Created by heaven7 on 2018/1/14.
 */
public interface MethodParameterSupplier {

    Object[] getParameters(InjectorRegistry registry, Object insertor, int methodTag);
}
