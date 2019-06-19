package com.heaven7.android.uiperformance;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.heaven7.core.util.WeakHandler;

public final class LogMonitor {

    private static final LogMonitor sInstance = new LogMonitor();
    private static final int MSG_LOG = 1;

    private static final long TIME_BLOCK = 25;
    private final HandlerThread mLogThread = new HandlerThread("log");
    private final InternalHandler mIoHandler;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new InternalHandler(mLogThread.getLooper(), this);
    }

    private final Runnable mLogRunnable = new Runnable() {
        final StringBuilder sb = new StringBuilder();
        @Override
        public void run() {
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.e("LogMonitor_TAG", sb.toString());
            sb.delete(0, sb.length());
        }
    };

    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
        return mIoHandler.hasMessages(MSG_LOG);
    }

    public void startMonitor() {
        Message msg = mIoHandler.obtainMessage(MSG_LOG);
        mIoHandler.sendMessageDelayed(msg, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeMessages(MSG_LOG);
    }

    private static class InternalHandler extends WeakHandler<LogMonitor> {

        public InternalHandler(Looper looper, LogMonitor logMonitor) {
            super(looper, logMonitor);
        }
        @Override
        public void handleMessage(Message msg) {
            get().mLogRunnable.run();
        }
    }

}