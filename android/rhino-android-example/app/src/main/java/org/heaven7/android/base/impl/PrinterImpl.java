package org.heaven7.android.base.impl;

import androidx.annotation.Keep;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Printer;
import com.heaven7.java.base.util.Throwables;

/**
 * Created by heaven7 on 2018/9/29 0029.
 */
@Keep
public class PrinterImpl implements Printer{

    @Override
    public void println(String x) {
        System.out.println(x);
    }
    @Override
    public void info(String tag, String method, String msg) {
        Logger.i(tag, method, msg);
    }
    @Override
    public void debug(String tag, String method, String msg) {
        Logger.d(tag, method, msg);
    }
    @Override
    public void warn(String tag, String method, Throwable t) {
        Logger.w(tag, method,  Throwables.getStackTraceAsString(t));
    }
    @Override
    public void warn(String tag, String method, String msg) {
        Logger.w(tag, method, msg);
    }
    @Override
    public void verbose(String tag, String method, String msg) {
        Logger.v(tag, method, msg);
    }
    @Override
    public void error(String tag, String method, String msg) {
        Logger.e(tag, method, msg);
    }
    @Override
    public void error(String tag, String method, Throwable t) {
        Logger.e(tag, method, Throwables.getStackTraceAsString(t));
    }
}
