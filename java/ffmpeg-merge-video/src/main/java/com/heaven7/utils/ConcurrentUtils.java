package com.heaven7.utils;

import java.util.concurrent.*;

/**
 * concurrent utils.
 * @author heaven7
 */
public class ConcurrentUtils {

    public static void shutDownNow() {
        //empty now
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

