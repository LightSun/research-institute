package com.heaven7.core.util;

import com.heaven7.java.base.util.DefaultPrinter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@link Logger#v(String, String, String)} i like
 *
 * @author heaven7
 */
public class Logger {

    private static int sLOG_LEVEL = 6;// default all

    public static final int VERBOSE = 5; /* the lowest */
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARNING = 2;
    public static final int ERROR = 1;
    /**default true(only called internal)*/
    public static boolean IsDebug = true;

    /** enable or disable debug
     * @param debug if debug */
    public static void setDebug(boolean debug) {
        IsDebug = debug;
        if (debug)
            setLevel(VERBOSE);
        else
            setLevel(INFO);
    }

    public static void setLevel(int lowestLevel) {
        switch (lowestLevel) {
            case VERBOSE:
                sLOG_LEVEL = 6;
                break;

            case DEBUG:
                sLOG_LEVEL = 5;
                break;

            case INFO:
                sLOG_LEVEL = 4;

                break;

            case WARNING:
            case ERROR:
                sLOG_LEVEL = 3;
                break;

            default:
                throw new IllegalArgumentException("caused by log lowestLevel="
                        + lowestLevel);
        }
    }

    // verbose
    public static void v(String tag, String msg) {
        if (sLOG_LEVEL > VERBOSE) {
           // Log.v(tag, msg);
            DefaultPrinter.getDefault().debug(tag, "", msg);
        }
    }

    public static void v(String tag, String methodTag, String msg) {
        if (sLOG_LEVEL > VERBOSE) {
           // Log.v(tag, "called [ " + methodTag + "() ]: " + msg);
            DefaultPrinter.getDefault().debug(tag, methodTag, msg);
        }
    }

    // debug
    public static void d(String tag, String msg) {
        if (sLOG_LEVEL > DEBUG) {
           // Log.d(tag, msg);
            DefaultPrinter.getDefault().debug(tag, "", msg);
        }
    }

    public static void d(String tag, String methodTag, String msg) {
        if (sLOG_LEVEL > DEBUG) {
           // Log.d(tag, "called [ " + methodTag + "() ]: " + msg);
            DefaultPrinter.getDefault().debug(tag, methodTag, msg);
        }
    }

    // info
    public static void i(String tag, String msg) {
        if (sLOG_LEVEL > INFO) {
          //  Log.i(tag, msg);
            DefaultPrinter.getDefault().info(tag, "", msg);
        }
    }

    public static void i(String tag, String methodTag, String msg) {
        if (sLOG_LEVEL > INFO) {
           // Log.i(tag, "called [ " + methodTag + "() ]: " + msg);
            DefaultPrinter.getDefault().info(tag, methodTag, msg);
        }
    }

    // warning
    public static void w(String tag, String msg) {
        if (sLOG_LEVEL > WARNING) {
          //  Log.w(tag, msg);
            DefaultPrinter.getDefault().warn(tag, "", msg);
        }
    }

    public static void w(String tag, String methodTag, String msg) {
        if (sLOG_LEVEL > WARNING) {
          //  Log.w(tag, "called [ " + methodTag + "() ]: " + msg);
            DefaultPrinter.getDefault().warn(tag, methodTag, msg);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (sLOG_LEVEL > WARNING) {
          //  Log.w(tag, throwable);
            DefaultPrinter.getDefault().warn(tag, "", toString(throwable));
        }
    }

    // error
    public static void e(String tag, String msg) {
        if (sLOG_LEVEL > ERROR) {
          //  Log.e(tag, msg);
            DefaultPrinter.getDefault().warn(tag, "==error==", msg);
        }
    }

    public static void e(String tag, String methodTag, String msg) {
        if (sLOG_LEVEL > ERROR) {
           // Log.e(tag, "called [ " + methodTag + "() ]: " + msg);
            DefaultPrinter.getDefault().warn(tag, methodTag, msg);
        }
    }


    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        t.printStackTrace(pw);
        Throwable cause = t.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.flush();
        String data = sw.toString();
        pw.close();
        return data;
    }

    public static String getLogInfo(String path){
        File file = new File(path);
        if(file.exists()){
            BufferedReader br =null;
            try {
                StringBuilder sb = new StringBuilder();
                br = new BufferedReader(new FileReader(file));
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }finally{
                if(br!=null)
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
            }
        }
        return null;
    }
}

