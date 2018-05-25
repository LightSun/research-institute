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
    private final String cmd_log;

    public CmdHelper(String... cmds) {
        this.cmds = cmds;
        StringBuilder sb = new StringBuilder();
        for(int i = 0 , size = cmds.length ; i < size ; i ++){
            sb.append(cmds[i]);
            if(i != size -1){
                sb.append(" ");
            }
        }
        cmd_log = sb.toString();
    }
    public void execute(){
        execute(new LogCallback());
    }
    public void execute(Callback callback){
        BufferedReader reader = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmds);
            pb.redirectErrorStream(true);
            callback.beforeStartCmd(this, pb);
            Process process = pb.start();
            process.waitFor();
            System.out.println(cmd_log + "  ,exitValue = " + process.exitValue());

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

        void beforeStartCmd(CmdHelper helper, ProcessBuilder pb);
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
        @Override
        public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {

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
        @Override
        public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {

        }
        /** in mill-seconds */
        public long getDuration(){
            return (long) (duration * 1000);
        }
    }
}
