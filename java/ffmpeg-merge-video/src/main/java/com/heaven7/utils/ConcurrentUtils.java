package com.heaven7.utils;

import java.util.concurrent.*;

/**
 * concurrent utils.
 * @author heaven7
 */
public class ConcurrentUtils {

    private static ExecutorService sService;

    public static void shutDownNow() {
        if (sService != null) {
            sService.shutdownNow();
            sService = null;
        }
    }

    public static void submit(Runnable r) {
        if (sService == null) {
            sService = new ThreadPoolExecutor(5, 8,
                    60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
        sService.submit(r);
    }

    public static void awaitBarrier(CyclicBarrier barrier){
        try {
            System.out.println("await...");
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
           new RuntimeException(e);
        }
    }
}

