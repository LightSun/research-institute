package com.heaven7.java.study.fake_share;

/**
 * 伪共享： jdk1.7解决.填充问题。
 * 把padding放在基类里面，可以避免优化。（这好像没有什么道理好讲的，JAVA7的内存优化算法问题，能绕则绕）。
 * 不过，这种办法怎么看都有点烦，借用另外一个博主的话：做个java程序员真难。
 */
public final class FalseSharing_JDK1_7 implements Runnable {
    public static int NUM_THREADS = 4; // change  
    public final static long ITERATIONS = 500L * 1000L * 1000L;  
    private final int arrayIndex;  
    private static VolatileLong[] longs;  
  
    public FalseSharing_JDK1_7(final int arrayIndex) {
        this.arrayIndex = arrayIndex;  
    }  
  
    public static void main(final String[] args) throws Exception {  
        Thread.sleep(10000);  
        System.out.println("starting....");  
        if (args.length == 1) {  
            NUM_THREADS = Integer.parseInt(args[0]);  
        }  
  
        longs = new VolatileLong[NUM_THREADS];  
        for (int i = 0; i < longs.length; i++) {  
            longs[i] = new VolatileLong();
        }  
        final long start = System.nanoTime();  
        runTest();  
        System.out.println("duration = " + (System.nanoTime() - start));  
    }  
  
    private static void runTest() throws InterruptedException {  
        Thread[] threads = new Thread[NUM_THREADS];  
        for (int i = 0; i < threads.length; i++) {  
            threads[i] = new Thread(new FalseSharing_JDK1_7(i));
        }  
        for (Thread t : threads) {  
            t.start();  
        }  
        for (Thread t : threads) {  
            t.join();  
        }  
    }  
  
    public void run() {  
        long i = ITERATIONS + 1;  
        while (0 != --i) {  
            longs[arrayIndex].value = i;  
        }  
    }

    public static class VolatileLongPadding {
        //填充缓存行
        public volatile long p1, p2, p3, p4, p5, p6; // 注释
    }
    public  static class VolatileLong extends VolatileLongPadding {
        public volatile long value = 0L;
    }
}