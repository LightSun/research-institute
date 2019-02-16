package com.heaven7.util;

import com.heaven7.utils.CmdHelper;

/**
 * @author heaven7
 */
public class FFmpegUtils2Test {

    public static void main(String[] args) {
        String inVideo = "E:\\work\\ui2\\movies\\display.mov";
        String outVideo = "E:\\work\\ui2\\movies\\display.mp4";
        CmdHelper cmdHelper = new CmdHelper(FFmpegUtils2.buildTransformFormatCmd2(inVideo, "2000k", "2500k",outVideo));
        System.out.println(cmdHelper.getCmdActually());
        cmdHelper.execute(new CmdHelper.InhertIoCallback());
       /* float value = (float) (Math.log(10) / Math.log(3)); //3^x = 10
        System.out.println(value);*/
    }
}
