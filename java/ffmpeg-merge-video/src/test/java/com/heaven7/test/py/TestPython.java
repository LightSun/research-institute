package com.heaven7.test.py;

import com.heaven7.utils.CmdHelper;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TestPython {

    public static void main(String[] args) {
        testCmdProxy();
    }

    private static void testCmdProxy() {
        String extraCmd = "python get_face_image.py %s %s".replaceAll(" ", "+");
        String input = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        String output = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\真丝吊带裙";
        final String env = "E:\\study\\github\\research-institute\\python\\Py_work\\src\\tag_with_face";

        String[] cmd = buildCmd("cmd_proxy.py", extraCmd, input, output);
        new CmdHelper(cmd).execute(new CmdHelper.LogCallback(){
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
