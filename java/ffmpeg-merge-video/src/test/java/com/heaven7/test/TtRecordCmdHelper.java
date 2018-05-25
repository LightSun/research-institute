package com.heaven7.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TtRecordCmdHelper {

    private static final String TAG = "TtRecordCmdHelper";

    public static void main(String[] args) {
        try {
            executeDir("F:\\videos\\故事线\\婚礼4\\晚宴");
            /*String file = "F:\\videos\\故事线\\婚礼4\\故事线测试素材\\GP5A0862.mp4";
            executeFile(file);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //generate whole dir of files
    public static void executeDir(String dir) throws Exception {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("python");
        cmds.add("process.py");
        cmds.add(dir);

        String[] arr = new String[cmds.size()];
        String[] cmdss = cmds.toArray(arr);

        ProcessBuilder pb = new ProcessBuilder(cmdss);
        pb.redirectErrorStream(true);
        pb.directory(new File("E:/work/ai_script"));
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }
    public static void executeFile(String file) throws Exception {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("python");
        cmds.add("process.py");
        cmds.add(file);

        String[] arr = new String[cmds.size()];
        String[] cmdss = cmds.toArray(arr);

        ProcessBuilder pb = new ProcessBuilder(cmdss);
        pb.redirectErrorStream(true);
        pb.directory(new File("E:/work/ai_script"));
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }
}
