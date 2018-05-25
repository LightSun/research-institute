package com.heaven7.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ProcessTest {

    public static void main(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            System.out.println(process.exitValue());
            byte[] bytes = new byte[process.getInputStream().available()];
            process.getInputStream().read(bytes);
            System.out.println(new String(bytes));

            //testAny("ffmpeg"); //ok

           // testNotePad();

            jumpDir();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     cmd /c dir 是执行完dir命令后关闭命令窗口。
     cmd /k dir 是执行完dir命令后不关闭命令窗口。
     cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。
     cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
     */
    private static void jumpDir() throws Exception {
    // ProcessBuilder pb2 = new ProcessBuilder("cmd.exe","/c","start","cmd");
      //  File file = new File("E:\\work\\ai_script\\tmp");
//"python process.py F:/videos/test_cut/test_shot_cut/%s" % file
        ProcessBuilder pb = new ProcessBuilder("cmd","/c","start","python","batch.py"); // /c 会回到java 。 /k不会返回到java
        pb.redirectErrorStream(true);
        pb.directory(new File("E:/work/ai_script"));
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }

    //记事本
    public static void testNotePad() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("notepad");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }

    public static void testAny(String... cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }
    public static void main2(String[] args) throws InterruptedException,
            IOException {
        //设置工作目录
        String[] env = { "JAVA_HOME=C:/Program Files/Java/jdk1.5.0_06",
                "PATH=C:/Program Files/Java/jdk1.5.0_06/bin" };
        //设置环境变量
        File dir = new File(
                "D:/eclipse2/workspace/RegExpProject/com/test/process");
        //显示当前Java运行环境信息
     /*   System.out.println(executeCommand(new String[] { "cmd", "/c", "java",
                "-version" }, env, dir));
        //调用当前版本的Java编译器编译源码
        System.out.println(executeCommand(new String[] { "cmd", "/c", "javac",
                "ProcessTest1.java" }, env, dir));
*/
        //  Process ps = Runtime.getRuntime().exec(cmd, env, dir);
    }

}
