package test.provide;

import org.junit.Test;

@Provider
public class MainActivity {

    private static final String TAG = "MainActivity";

    InjectorKnife.Injector injector;

    public MainActivity() {
        injector = InjectorKnife.create(this, new Inlay());
    }

    //最终可以字节码注入
    @Test
    @ProvideMethod(__$Flags.FLAG_ON_CREATE)
    public void onCreate(){
        injector.inject(__$Flags.FLAG_ON_CREATE);
    }
    @Test
    @ProvideMethod(__$Flags.FLAG_ON_START)
    public void onStart(){
        injector.inject(__$Flags.FLAG_ON_START);
    }
    @Test
    @ProvideMethod(__$Flags.FLAG_ON_DESTROY)
    public void onDestroy(){
        injector.inject(__$Flags.FLAG_ON_DESTROY);
    }

    public static class  __$Flags{
        public static final int FLAG_ON_CREATE  = 1;
        public static final int FLAG_ON_START   = 2;
        public static final int FLAG_ON_DESTROY = 4;
    }
}
