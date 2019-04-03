package com.heaven7.java.pc.schedulers;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.heaven7.java.base.util.Disposable;
import com.heaven7.java.base.util.Scheduler;

import java.util.concurrent.TimeUnit;

public class AndroidMainScheduler implements Scheduler {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public Scheduler.Worker newWorker() {
        return new MainWorker(mHandler);
    }

    private static class MainWorker implements Worker{

        final Handler mHandler;

        public MainWorker(Handler mHandler) {
            this.mHandler = mHandler;
        }
        @Override
        public Disposable schedule(Runnable task) {
            TaskWrapper wrapper = new TaskWrapper(mHandler, task, 0);
            wrapper.post();
            return wrapper;
        }
        @Override
        public Disposable scheduleDelay(Runnable task, long delay, TimeUnit unit) {
            TaskWrapper wrapper = new TaskWrapper(mHandler, task, 0);
            wrapper.postDelay(unit.toMillis(delay));
            return wrapper;
        }
        @Override
        public Disposable schedulePeriodically(Runnable task, long initDelay, long period, TimeUnit unit) {
            TaskWrapper wrapper = new TaskWrapper(mHandler, task, unit.toMillis(period));
            wrapper.postDelay(unit.toMillis(initDelay));
            return wrapper;
        }
    }
    private static class TaskWrapper implements Runnable, Disposable{
        final Handler mHandler;
        final Runnable task;
        final long period;

        public TaskWrapper(Handler mHandler, Runnable task, long period) {
            this.mHandler = mHandler;
            this.task = task;
            this.period = period;
        }
        public void post(){
            mHandler.post(this);
        }
        public void postDelay(long delay){
            mHandler.postDelayed(this, delay);
        }
        @Override
        public void dispose() {
            mHandler.removeCallbacks(this);
        }
        @Override
        public void run() {
            if(period > 0){
                long start = SystemClock.elapsedRealtime();
                task.run();
                long cost = SystemClock.elapsedRealtime() - start;
                mHandler.postDelayed(this, period - cost);
            }else {
                task.run();
            }
        }
    }
}
