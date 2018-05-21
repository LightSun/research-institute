package com.heaven7.test;

import java.io.File;

/**
 * (跑视频tag时)
 * 修改python 生成ttf文件的文件名
 */
public class ModifyFileName_TTF {

    public static void main(String[] args) {
        String path = "E:\\work\\ai_script\\'GP5A0859_output.tfrecord'";

        File file = new File(path);
        int index1 = path.indexOf("'");
        int index2 = path.lastIndexOf("'");
        String prefix = path.substring(0, index1);

        String str = path.substring(index1 + 1, index2).toLowerCase();
        boolean result = file.renameTo(new File(prefix + str));
        System.out.println("rename " + (result ? "success" : "failed"));
    }
}
