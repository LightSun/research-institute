package com.heaven7.java.study.longaddr;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAddrTest {

    public static final int MAX_THREADS  = 3;
    public static final int TASK_COUNT   = 3;
    public static final int TARGET_COUNT = 10000000;

    private AtomicLong account = new AtomicLong(0L);
    private LongAdder laccount = new LongAdder();
    private long count = 0;

    static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);

    protected synchronized long inc(){
        return ++count;
    }
    protected synchronized long getCount(){
        return count;
    }

    public void testSync() throws InterruptedException{
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        SyncThread sync = new SyncThread(this, starttime);
        for(int i = 0 ; i < TASK_COUNT ; i++){
            service.submit(sync);
        }
        cdlsync.await();
        service.shutdown();
    }
    public void testAtomic() throws InterruptedException{
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        AtomicThread atomic = new AtomicThread(starttime);
        for(int i = 0 ; i < TASK_COUNT ; i++){
            service.submit(atomic);
        }
        cdlatomic.await();
        service.shutdown();
    }
    public void testLongAddr() throws InterruptedException{
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        LongAddrThread addr = new LongAddrThread(starttime);
        for(int i = 0 ; i < TASK_COUNT ; i++){
            service.submit(addr);
        }
        cdladdr.await();
        service.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        LongAddrTest test = new LongAddrTest();
        test.testSync(); //300 -340
        test.testAtomic(); //148-218
        test.testLongAddr(); //237- 266
    }

    class SyncThread implements Runnable{
        protected String name;
        protected long starttime;
        LongAddrTest out;

        public SyncThread(LongAddrTest out, long starttime) {
            this.starttime = starttime;
            this.out = out;
        }
        @Override
        public void run() {
            long v = out.getCount();
            while (v <TARGET_COUNT){
                v = out.inc();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("SyncThread spend: " + (endtime - starttime) + "ms" + " v = " + v);
            cdlsync.countDown();
        }
    }

    class AtomicThread implements Runnable{
        protected String name;
        protected long starttime;

        public AtomicThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = account.get();
            while (v <TARGET_COUNT){
                v = account.incrementAndGet();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("AtomicThread spend: " + (endtime - starttime) + "ms" + " v = " + v);
            cdlatomic.countDown();
        }
    }
    class LongAddrThread implements Runnable{
        protected String name;
        protected long starttime;

        public LongAddrThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = laccount.sum();
            while (v <TARGET_COUNT){
                laccount.increment();
                v = laccount.sum();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("LongAddrThread spend: " + (endtime - starttime) + "ms" + " v = " + v);
            cdladdr.countDown();
        }
    }
}
