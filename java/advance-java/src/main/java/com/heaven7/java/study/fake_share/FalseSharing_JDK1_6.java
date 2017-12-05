package com.heaven7.java.study.fake_share;

//伪共享（fake_share） .jdk1.6解决方案

/**
 * VolatileLong通过填充一些无用的字段p1,p2,p3,p4,p5,p6，再考虑到对象头也占用8bit, 刚好把对象占用的内存扩展到刚好占64bytes（或者64bytes的整数倍）。
 * 这样就避免了一个缓存行中加载多个对象。
 * 但这个方法现在只能适应JAVA6 及以前的版本了。因为JAVA 7会优化掉无用的字段，可以参考[《False Sharing && Java 7》](http://ifeve.com/false-shareing-java-7-cn/)。

 */
public final class FalseSharing_JDK1_6
        implements Runnable {
    public final static int NUM_THREADS = 4; // change 
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }
    public FalseSharing_JDK1_6(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing_JDK1_6(i));
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

    public final static class VolatileLong {
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6; // comment out 
    }
}