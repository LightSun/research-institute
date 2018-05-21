package com.heaven7.utils;

import com.heaven7.java.base.util.DefaultPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * the command helper.
 * @author heaven7
 */
public class CmdHelper {

    private final String[] cmds;

    public CmdHelper(String... cmds) {
        this.cmds = cmds;
    }

    public void execute(Callback callback){
        BufferedReader reader = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmds);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            System.out.println(process.exitValue());

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            callback.onStart(this);
            String line;
            while( (line = reader.readLine()) != null){
                callback.collect(this, line);
            }
            callback.onEnd(this);
        }catch (Exception e){
            callback.onFailed(this, e);
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                   // e.printStackTrace();
                }
            }
        }
    }

    public interface Callback{
        void collect(CmdHelper helper, String line);
        void onFailed(CmdHelper helper, Exception e);
        void onStart(CmdHelper helper);

        void onEnd(CmdHelper helper);
    }

    public static class LogCallback implements Callback{
        final StringBuilder sb = new StringBuilder();
        @Override
        public void collect(CmdHelper helper, String line) {
            sb.append(line);
        }
        @Override
        public void onFailed(CmdHelper helper, Exception e) {
            DefaultPrinter.getDefault().warn("LogCallback", "onFailed", e);
        }
        @Override
        public void onStart(CmdHelper helper) {
            sb.delete(0, sb.length());
        }
        @Override
        public void onEnd(CmdHelper helper) {

        }
    }

    public static class VideoDurationCallback implements Callback{

        float duration = -1; //in seconds
        @Override
        public void collect(CmdHelper helper, String line) {
             duration = Float.parseFloat(line);
        }
        @Override
        public void onFailed(CmdHelper helper, Exception e) {
            DefaultPrinter.getDefault().warn("VideoDurationCallback", "onFailed", e);
        }
        @Override
        public void onStart(CmdHelper helper) {

        }
        @Override
        public void onEnd(CmdHelper helper) {

        }
        /** in mill-seconds */
        public long getDuration(){
            return (long) (duration * 1000);
        }
    }
}
