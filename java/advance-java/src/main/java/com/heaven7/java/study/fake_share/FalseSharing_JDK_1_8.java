package com.heaven7.java.study.fake_share;

import sun.misc.Contended;

/**
 * 在JAVA 8中，缓存行填充终于被JAVA原生支持了。JAVA 8中添加了一个@Contended的注解，添加这个的注解，将会在自动进行缓存行填充。
 * 执行时，必须加上虚拟机参数-XX:-RestrictContended，@Contended注释才会生效。很多文章把这个漏掉了，那样的话实际上就没有起作用。

 {@literal @}Contended注释还可以添加在字段上，
 */
public final class FalseSharing_JDK_1_8 implements Runnable {
    public static int NUM_THREADS = 4; // change  
    public final static long ITERATIONS = 500L * 1000L * 1000L;  
    private final int arrayIndex;  
    private static VolatileLong[] longs;  
  
    public FalseSharing_JDK_1_8(final int arrayIndex) {
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
            threads[i] = new Thread(new FalseSharing_JDK_1_8(i));
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

    //
    @Contended
    public static class VolatileLong {
        public volatile long value = 0L;
    }


    public class PaddedObject {
        @Contended
        public volatile long valueA;
        public volatile long valueB;
    }
}