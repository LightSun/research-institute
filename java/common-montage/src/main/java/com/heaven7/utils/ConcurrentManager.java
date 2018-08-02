package com.heaven7.utils;

import java.util.concurrent.*;

public class ConcurrentManager {

    private static final int CORE_SIZE = 8;
    private static final int MAX_SIZE  = 16;

    private final ExecutorService mCoreService = new ThreadPoolExecutor(CORE_SIZE,
            MAX_SIZE, 60,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    private static class Creator{
         static final ConcurrentManager INSTANCE = new ConcurrentManager();
    }
    private ConcurrentManager(){}

    public static ConcurrentManager getDefault(){
        return Creator.INSTANCE;
    }

    public void submit(Runnable r){
        mCoreService.submit(r);
    }

    public <R> R submit(Callable<R> r) throws ExecutionException {
        Future<R> future = mCoreService.submit(r);
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public <R> R submitWithoutException(Callable<R> r){
        try {
            return submit(r);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
