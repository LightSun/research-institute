package test.provide;

import test.provide.framework.InjectObserver;

public class Inlay implements InjectObserver {

    @Insertor(MainActivity__$Flags.FLAG_onDestroy | MainActivity__$Flags.FLAG_onStart)
    public void every(){
        System.out.println("every");
    }

    @Insertor(MainActivity__$Flags.FLAG_onCreate)
    public void init(){
        System.out.println("init");
    }

    @Insertor(MainActivity__$Flags.FLAG_onStart)
    public void start(){
        System.out.println("start");
    }

    @Insertor(MainActivity__$Flags.FLAG_onDestroy)
    public void destroy(){
        System.out.println("destroy");
    }

}
