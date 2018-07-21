package com.heaven7.test.py;

import com.heaven7.TimeRecorder;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * springboot - server 占用5%内存
 */
public class TestPython {

    public static void main(String[] args) {
        // testCmdProxy();
        testConcurrent();
    }

    //E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\仙鹤无袖背心\\VID_20180507_145503.mp4
    // E:\BaiduNetdiskDownload\taobao_service\东森（服装）\女装南泉外拍第二次视频2\仙鹤无袖背心
    // E:\BaiduNetdiskDownload\taobao_service\东森（服装）\女装南泉外拍第二次视频2\扎染褙子\\VID_20180507_140122.mp4
    private static void testConcurrent() {
        final String env = "E:\\study\\github\\research-institute\\python\\Py_work\\src\\tag_with_face";
        String extraCmd = "python video2tfrecord.py %s %s".replaceAll(" ", "+");
        String outDir = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子";

        String dir = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子";
        List<String> files = FileUtils.getFiles(new File(dir), "mp4");
        for (String file : files) {
            String[] cmd = buildCmd("cmd_proxy.py", extraCmd, file, outDir);
            /*new CmdHelper(cmd).execute(new CmdHelper.LogCallback(){
                @Override
                public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                    pb.directory(new File(env));
                }
            });*/
            new Thread(new Runnable() {
                final TimeRecorder tr = new TimeRecorder(file);
                @Override
                public void run() {
                    tr.begin();
                    new CmdHelper(cmd).execute(new CmdHelper.LogCallback() {
                        @Override
                        public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                            pb.directory(new File(env));
                        }
                    });
                    tr.end();
                    System.out.println("time : " + tr.toString());
                }
            }).start();
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testGenTfrecord() {
        String extraCmd = "python get_face_image.py %s %s".replaceAll(" ", "+");
        String input = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        String output = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        final String env = "E:\\study\\github\\research-institute\\python\\Py_work\\src\\tag_with_face";

        String[] cmd = buildCmd("cmd_proxy.py", extraCmd, input, output);
        new CmdHelper(cmd).execute(new CmdHelper.LogCallback() {
            @Override
            public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                pb.directory(new File(env));
            }
        });
    }

    private static void testCmdProxy() {
        String extraCmd = "python get_face_image.py %s %s".replaceAll(" ", "+");
        String input = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        String output = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        final String env = "E:\\study\\github\\research-institute\\python\\Py_work\\src\\tag_with_face";

        String[] cmd = buildCmd("cmd_proxy.py", extraCmd, input, output);
        new CmdHelper(cmd).execute(new CmdHelper.LogCallback() {
            @Override
            public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                pb.directory(new File(env));
            }
        });
    }

    private static String[] buildCmd(String cmd_proxy_file, String extraCmd, String input, String output) {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("/wait");

        cmds.add("python");
        cmds.add(cmd_proxy_file);
        cmds.add(extraCmd);
        cmds.add("-input=" + input);
        cmds.add("-output=" + output);

        return cmds.toArray(new String[cmds.size()]);
    }
}
