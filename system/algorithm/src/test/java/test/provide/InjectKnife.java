package test.provide;

import test.provide.framework.InjectorRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InjectKnife {

    private static final WeakHashMap<Class<? extends InjectorRegistry>, Class<?>> sGenClassMap;

    static {
        sGenClassMap = new WeakHashMap<>();
    }

    public static Injector from(InjectorRegistry registry){
        return new Injector(getGenClass(registry));
    }

    public static Injector from(InjectorRegistry registry, Object insertor){
        return from(registry).with(insertor);
    }

    private static Class<?> getGenClass(InjectorRegistry provider) {
        Class<? extends InjectorRegistry> clazz = provider.getClass();
        Class<?> target = sGenClassMap.get(clazz);
        if(target != null){
            return target;
        }else {
            Provider pro = getProvider(clazz);
            if (pro == null) {
                throw new NullPointerException();
            }
            target = pro.value();
            sGenClassMap.put(clazz, target);
        }
        return target;
    }

    private static Provider getProvider(Class<?> clazz) {
        Provider pro;
        do{
            pro = clazz.getAnnotation(Provider.class);
            clazz = clazz.getSuperclass();
        }while (pro == null && !clazz.getName().startsWith("java.")
                && !clazz.getName().startsWith("android."));
        return pro;
    }

    private static void logCallStack() {
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            System.out.println("index=" + i + "----------------------------------");
            System.out.println("className=" + stackTraceElement.getClassName());
            //System.out.println("fileName=" + stackTraceElement.getFileName());
            System.out.println("methodName=" + stackTraceElement.getMethodName());
           // System.out.println("lineNumber=" + stackTraceElement.getLineNumber());
        }
    }

    public static class Injector{
        private static final String PREX_FLAG = "FLAG_";
        private static final WeakHashMap<Class<?>, List<Method>> sMethodsMap; //insertor, methods
        private final ArrayList<Object> insertors;
        private final Class<?> mFlagClass;

        static {
            sMethodsMap = new WeakHashMap<>();
        }

        Injector(Class<?> genClass) {
            this.insertors = new ArrayList<>();
            mFlagClass = genClass;
        }

        public Injector with(Object insertor){
            insertors.add(insertor);
            Class<?> clazz = insertor.getClass();
            if(sMethodsMap.get(clazz) == null) {
                List<Method> methods = StreamSupport.stream( Arrays.spliterator(clazz.getMethods()), false)
                        .filter(method -> (method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC
                                && method.getAnnotation(Insertor.class) != null
                ).collect(Collectors.toList());
                sMethodsMap.put(clazz, methods);
            }
            return this;
        }

        public void inject(Object...params){
           // logCallStack();
            final String callMethodName = getCallMethodName();
            int flag;
            try {
                Field field = mFlagClass.getField(PREX_FLAG + callMethodName);
                flag = (int) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return;
            }
            //filter callMethodName

            insertors.forEach(o ->
                    sMethodsMap.get(o.getClass()).stream().filter(
                        method -> (method.getAnnotation(Insertor.class).value() & flag) == flag
                 ).forEachOrdered(method -> {
                    try {
                        method.invoke(o, params);
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                })
            );
        }

        private static String getCallMethodName(){
            /**
             * 0 is the
             */
            StackTraceElement[] stes = new Throwable().getStackTrace();
            return stes[2].getMethodName();
        }
    }
}
