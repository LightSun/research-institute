package test.provide;

public class Inlay {

    @Insertor(MainActivity.__$Flags.FLAG_ON_DESTROY | MainActivity.__$Flags.FLAG_ON_START)
    public void every(){
        System.out.println("every");
    }

    @Insertor(MainActivity.__$Flags.FLAG_ON_CREATE)
    public void init(){
        System.out.println("init");
    }

    @Insertor(MainActivity.__$Flags.FLAG_ON_START)
    public void start(){
        System.out.println("start");
    }

    @Insertor(MainActivity.__$Flags.FLAG_ON_DESTROY)
    public void destroy(){
        System.out.println("destroy");
    }

}
