package com.heaven7.test;

import com.heaven7.java.base.util.Logger;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * help protoc cmd
 */
public class ProtocHelper {

    private static final String TAG = "ProtocHelper";
    private static final String ENV = "E:\\study\\github\\THIRDS\\models\\research";
    private static final String PROTOC_INPUT_DIR = "E:\\study\\github\\THIRDS\\models\\research\\object_detection\\protos";
    private static final String PROTOC_OUT_DIR = ".";

    //protoc object_detection\protos\train.proto --python_out=.
    @Test
    public void testProto(){
        List<String> files = FileUtils.getFiles(new File(PROTOC_INPUT_DIR), "proto");
        SimpleCallback callback = new SimpleCallback();
        for (String file : files){
            Logger.d(TAG, "testProto", "file = " + file);
            String relativePath = file.substring(file.indexOf(ENV) + 1 + ENV.length());
           // System.out.println(relativePath);
            new CmdHelper(toCmd(relativePath)).execute(callback);
        }
    }

  /*  public static void main(String[] args) {
         //测试urlencode 和python交互
        String path="F:\\videos\\故事线\\婚礼2\\晚宴\\辅导费.mp4";
        System.out.println(URLEncoder.encode(path).equals("F%3A%5Cvideos%5C%E6%95%85%E4%BA%8B%E7%BA%BF%5C%E5%A9%9A%E7%A4%BC2%5C%E6%99%9A%E5%AE%B4%5C%E8%BE%85%E5%AF%BC%E8%B4%B9.mp4"));
    }
*/
    private static String[] toCmd(String file){
        List<String> list = new ArrayList<>();
        list.add("cmd");
        list.add("/c");
        list.add("start");
        list.add("/wait");
        list.add("protoc");
        list.add(file);
        list.add("--python_out=" + PROTOC_OUT_DIR);

        String[] cmds = list.toArray(new String[list.size()]);
        return cmds;
    }

    private static class SimpleCallback extends CmdHelper.LogCallback{
        @Override
        public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
            super.beforeStartCmd(helper, pb);
            pb.directory(new File(ENV));
        }
    }
}
