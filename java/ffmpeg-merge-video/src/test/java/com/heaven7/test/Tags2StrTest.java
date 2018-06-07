package com.heaven7.test;

import com.heaven7.ve.test.util.FileHelper;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 将tag的id值变成具体的名称
 */
public class Tags2StrTest {

    @Test
    public void test(){
        String dir = "F:\\videos\\jinliang\\tb女装\\tags";
      /*  List<String> paths = new ArrayList<>();
        FileHelper.getFiles(new File(dir), "csv", paths);
        try {
            for(String path : paths){
                execute(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            execute(dir);
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * python tags2.py 0.5 F:\videos\jinliang\归档1_tags/2_predictions.csv
     * @throws Exception
     */
    public static void execute(String file) throws Exception {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("python");
        cmds.add("tags2.py");
        cmds.add("0.5");
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
