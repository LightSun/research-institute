package com.heaven7.ve.collect;

import com.heaven7.core.util.Logger;

/**
 * the performance writer
 * @author heaven7
 */
public interface PerformanceWriter {

    PerformanceWriter LOG_WRITER = new PerformanceWriter() {
        @Override
        public void writeMessage(String tag, String func, String msg) {
            Logger.d(tag, func, msg);
        }
        @Override
        public void writeMessage(String tag, String msg) {
            Logger.d(tag, msg);
        }
    };

    void writeMessage(String tag, String func, String msg);

    void writeMessage(String tag, String msg);
}