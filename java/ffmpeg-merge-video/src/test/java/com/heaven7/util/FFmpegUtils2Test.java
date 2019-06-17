package com.heaven7.util;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * @author heaven7
 */
public class FFmpegUtils2Test {

    public static void main(String[] args) {
        //test1();
        String inDir = "E:\\tmp\\bugfinds\\right_music2\\60s";
        String outDir = "E:\\tmp\\bugfinds\\wav\\60s";
        transformAll(inDir, outDir);
    }

    private static void test1() {
        String inVideo = "E:\\work\\ui2\\movies\\display.mov";
        String outVideo = "E:\\work\\ui2\\movies\\display.mp4";
        CmdHelper cmdHelper = new CmdHelper(FFmpegUtils2.buildTransformFormatCmd2(inVideo, "2000k", "2500k",outVideo));
        System.out.println(cmdHelper.getCmdActually());
        cmdHelper.execute(new CmdHelper.InhertIoCallback());
       /* float value = (float) (Math.log(10) / Math.log(3)); //3^x = 10
        System.out.println(value);*/
    }

    private static void transformAll(String inDir, String outDir){
        List<String> files = FileUtils.getFiles(new File(inDir), "mp3");
        for (String file : files){
           // String dir = FileUtils.getFileDir(file, 1, true);
            String fileName = FileUtils.getFileName(file);
            String target = outDir + File.separator + fileName + ".wav";

            CmdHelper cmdHelper = new CmdHelper(FFmpegUtils2.buildTransformFormatCmd(file, target));
            System.out.println(cmdHelper.getCmdActually());
            cmdHelper.execute(new CmdHelper.InhertIoCallback());
        }
    }
}
