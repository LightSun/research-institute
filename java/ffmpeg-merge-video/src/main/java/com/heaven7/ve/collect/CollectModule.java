package com.heaven7.ve.collect;

/**
 * @author heaven7
 */
public interface CollectModule {

    PerformanceWriter getWriter();

    void addMessage(String tag, String method, String msg);

    void start(String label);

    void end(String label);
}
