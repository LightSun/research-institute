package com.heaven7.util;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.base.util.threadpool.Executors2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author heaven7
 */
public class SharedThreadPool {

    public static final String TAG = "SharedPool";
    private final List<WeakReference<Object>> mMemberList;
    private ExecutorService mService;
    private Callback mCallback;

    public SharedThreadPool(){
        this(new SimpleCallback());
    }
    public SharedThreadPool(Callback callback) {
        this.mMemberList = new ArrayList<>(6);
        this.mCallback = callback;
        this.mService = callback.onCreateExecutorService();
    }

    /**
     * add a member which will hold the share thread pool.
     *
     * @param member the member.
     */
    public void addMember(Object member) {
        Throwables.checkNull(member);
        mMemberList.add(new WeakReference<>(member));
    }

    public boolean removeMember(Object member){
        if(mService == null){
            return false;
        }
        boolean result = hasMember0(member, true);
        gc();
        if(mMemberList.isEmpty()){
            destroyService(mCallback.onDestroyService(mService));
            mCallback = null;
        }
        return result;
    }

    public void destroy(){
        if(mCallback != null && mService != null) {
            destroyService(mCallback.onDestroyService(mService));
            mMemberList.clear();
            mCallback = null;
        }
    }

    public boolean isActive(){
        return mService != null;
    }

    public boolean hasMember(Object member) {
        return hasMember0(member, false);
    }

    public boolean submit(Runnable task){
        if(mService != null){
            Future<?> future = mService.submit(task);
            if(mCallback != null){
                mCallback.onSubmitTask(getAliveMembers(), task, future);
            }
            return true;
        }
        return false;
    }

    private boolean hasMember0(Object member, boolean remove) {
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for(;it.hasNext();){
            WeakReference<Object> wrf = it.next();
            Object o = wrf.get();
            if(o != null && o == member){
                if(remove){
                    it.remove();
                }
                return true;
            }
        }
        return false;
    }

    private void destroyService(boolean rightNow){
        if(mService != null) {
            if (rightNow) {
                mService.shutdownNow();
            }else{
                mService.shutdown();
            }
            mService = null;
        }
    }

    private void gc(){
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for(;it.hasNext();){
            WeakReference<Object> wrf = it.next();
            if(wrf.get() == null){
                it.remove();
            }
        }
    }
    private List<Object> getAliveMembers(){
        List<Object> members = new ArrayList<>();
        Iterator<WeakReference<Object>> it = mMemberList.iterator();
        for(;it.hasNext();){
            Object o = it.next().get();
            if(o != null){
                members.add(o);
            }else{
               it.remove();
            }
        }
        return members;
    }

    public interface Callback{
        /**
         * called on destroy service. after call this. {@linkplain ExecutorService#shutdown()} or
         * {@linkplain ExecutorService#shutdownNow()} will be called.
         * @param service the service
         * @return true if should destroy the service right now. false otherwise.
         */
         boolean onDestroyService(ExecutorService service);

        /**
         * called on create executor service.
         * @return the service
         */
         ExecutorService onCreateExecutorService();

        /**
         * called on submit the task
         * @param aliveMembers the alive members
         * @param task the task
         * @param future the future of the task.
         */
        void onSubmitTask(List<Object> aliveMembers, Runnable task, Future<?> future);
    }

    public static class SimpleCallback implements Callback{
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
