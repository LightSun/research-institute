package com.heaven7.ve.utils;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.base.util.threadpool.Executors2;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author heaven7
 */
public class SharedThreadPool {

    public static final int UNKNOWN = -1;
    public static final String TAG = "SharedPool";
    private final List<WeakReference<Object>> mMemberList;
    private TaskObserver mTaskObserver;
    private ExecutorService mService;
    private Callback mCallback;

    public SharedThreadPool() {
        this(new SimpleCallback());
    }

    public SharedThreadPool(Callback callback) {
        this.mMemberList = new ArrayList<>(6);
        this.mCallback = callback;
        this.mService = callback.onCreateExecutorService();
    }

    public void setTaskObserver(TaskObserver observer) {
        this.mTaskObserver = observer;
    }

    /**
     * add a member which will hold the share thread pool.
     *
     * @param member the member.
     */
    public void addMember(Object member) {
        Throwables.checkNull(member);
        if (!isActive()) {
            this.mService = mCallback.onCreateExecutorService();
        }
        mMemberList.add(new WeakReference<>(member));
    }

    public boolean removeMember(Object member) {
        if (mService == null) {
            return false;
        }
        boolean result = hasMember0(member, true);
        gc();
        if (mMemberList.isEmpty()) {
            destroyService(mCallback.onDestroyService(mService));
            mCallback = null;
        }
        return result;
    }

    public void destroy() {
        if (mCallback != null && mService != null) {
            destroyService(mCallback.onDestroyService(mService));
            mMemberList.clear();
            mCallback = null;
        }
    }

    public boolean isActive() {
        return mService != null;
    }

    public boolean hasMember(Object member) {
        return hasMember0(member, false);
    }

    public boolean submit(Runnable task) {
        return submit(UNKNOWN, "", task, null);
    }
    public boolean submit(String mark, Runnable task) {
        return submit(UNKNOWN, mark, task, null);
    }
    public boolean submit(int module, String mark, Runnable task) {
        return submit(module, mark, task, null);
    }

    public boolean submit(int module, String mark, Runnable task, @Nullable TaskObserver companionObserver) {
        if (mService != null) {
            Future<?> future = mService.submit(new WrapTask(module, mark, task,
                    GroupTaskObserver.of(mTaskObserver, companionObserver)));
            if (mCallback != null) {
                mCallback.onSubmitTask(getAliveMembers(), task, future);
            }
            return true;
        }
        return false;
    }

    public List<Object> getAliveMembers() {
        List<Object> members = new ArrayList<>();
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for (; it.hasNext(); ) {
            Object o = it.next().get();
            if (o != null) {
                members.add(o);
            } else {
                it.remove();
            }
        }
        return members;
    }

    private boolean hasMember0(Object member, boolean remove) {
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for (; it.hasNext(); ) {
            WeakReference<Object> wrf = it.next();
            Object o = wrf.get();
            if (o != null && o == member) {
                if (remove) {
                    it.remove();
                }
                return true;
            }
        }
        return false;
    }

    private void destroyService(boolean rightNow) {
        if (mService != null) {
            if (rightNow) {
                mService.shutdownNow();
            } else {
                mService.shutdown();
            }
            mService = null;
        }
    }

    private void gc() {
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for (; it.hasNext(); ) {
            WeakReference<Object> wrf = it.next();
            if (wrf.get() == null) {
                it.remove();
            }
        }
    }

    private static class WrapTask implements Runnable {
        final int module;
        final String mark;
        final Runnable base;
        final TaskObserver observer;

        WrapTask(int module, String mark, Runnable base, TaskObserver observer) {
            this.module = module;
            this.mark = mark;
            this.base = base;
            this.observer = observer;
        }

        @Override
        public void run() {
            if (observer != null) {
                observer.onTaskStart(module, mark, base);
                base.run();
                observer.onTaskEnd(module, mark, base);
            } else {
                base.run();
            }
        }
    }

    public static class GroupTaskObserver implements TaskObserver {

        private final Set<TaskObserver> observers = new HashSet<>();

        public static GroupTaskObserver of(TaskObserver... observers) {
            GroupTaskObserver gto = new GroupTaskObserver();
            for (TaskObserver observer : observers) {
                if (observer != null) {
                    gto.addTaskObserver(observer);
                }
            }
            return gto;
        }

        public void addTaskObserver(TaskObserver observer) {
            observers.add(observer);
        }

        @Override
        public void onTaskStart(int module, String mark, Runnable r) {
            for (TaskObserver observer : observers) {
                observer.onTaskStart(module, mark, r);
            }
        }

        @Override
        public void onTaskEnd(int module, String mark, Runnable r) {
            for (TaskObserver observer : observers) {
                observer.onTaskEnd(module, mark, r);
            }
        }
    }

    /**
     * the task observer
     */
    public interface TaskObserver {
        /**
         * called on task start
         *
         * @param module the module
         * @param mark   the mark of this task
         * @param r      the task which was submitted
         */
        void onTaskStart(int module, String mark, Runnable r);

        /**
         * called on task end
         *
         * @param module the module
         * @param mark   the mark of this task
         * @param r      the task which was submitted
         */
        void onTaskEnd(int module, String mark, Runnable r);
    }

    public interface Callback {
        /**
         * called on destroy service. after call this. {@linkplain ExecutorService#shutdown()} or
         * {@linkplain ExecutorService#shutdownNow()} will be called.
         *
         * @param service the service
         * @return true if should destroy the service right now. false otherwise.
         */
        boolean onDestroyService(ExecutorService service);

        /**
         * called on create executor service.
         *
         * @return the service
         */
        ExecutorService onCreateExecutorService();

        /**
         * called on submit the task
         *
         * @param aliveMembers the alive members
         * @param task         the task
         * @param future       the future of the task.
         */
        void onSubmitTask(List<Object> aliveMembers, Runnable task, Future<?> future);
    }

    public static class SimpleCallback implements Callback {
        @Override
        public boolean onDestroyService(ExecutorService service) {
            return true;
        }

        @Override
        public ExecutorService onCreateExecutorService() {
            return Executors2.newCachedThreadPool(new DefaultThreadFactory(TAG, true));
        }

        @Override
        public void onSubmitTask(List<Object> aliveMembers, Runnable task, Future<?> future) {
        }
    }
}
