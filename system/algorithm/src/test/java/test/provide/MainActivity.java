package test.provide;

import org.junit.Test;
import test.provide.framework.InjectorRegistry;

@Provider(MainActivity__$Flags.class)
public class MainActivity implements InjectorRegistry{

    private static final String TAG = "MainActivity";

    private final InjectKnife.Injector injector;

    public MainActivity() {
        injector = InjectKnife.from(this, new Inlay());
    }

    //最终可以字节码注入
    @Test
    @ProvideMethod()
    public void onCreate(){
        getInjector().inject();
    }
    @Test
    @ProvideMethod()
    public void onStart(){
        getInjector().inject();
    }
    @Test
    @ProvideMethod()
    public void onDestroy(){
        getInjector().inject();
    }

    @Override
    public InjectKnife.Injector getInjector() {
        return injector;
    }
}
