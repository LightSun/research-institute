package com.heaven7.test;

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

            String cmd = "ffmpeg -i F:/videos/test_cut/test_shot_cut/GP5A0859.mp4 -vcodec copy -acodec copy -ss 00:00:00.00 -to 00:02:48.00 E:/study/github/ffmpeg-merge-video/cut_videos/GP5A0859_0.00__168.00.mp4 -y";
            testAny(cmd); // 找不到指定文件
           // testNotePad();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public static void testAny(String cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }

}
