package test.provide;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InjectorKnife {


    public static Injector create(Object provider, Object insertor){
        return new Injector(provider, insertor);
    }

    public static class Injector{
        final Object provider;
        final Object insertor;
        private final Spliterator<Method> mSplitor;

        public Injector(Object provider, Object insertor) {
            this.provider = provider;
            this.insertor = insertor;
            mSplitor = Arrays.spliterator(insertor.getClass().getMethods());
        }

        public void inject(int flag, Object...params){
            StreamSupport.stream(mSplitor, false)
                    .filter((method -> {
                        if((method.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC)
                            return false;
                        Insertor insertor = method.getAnnotation(Insertor.class);
                        return insertor != null && (insertor.value() & flag) == flag;
                    }))
                    /*.sorted( (m1, m2)-> m1.get)*/
                    .forEach(method -> {
                        try {
                            method.invoke(insertor, params);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                     }
             );
            /*(method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC &&
                            method.getAnnotation(Insertor.class) != null)*/
        }
    }
}
