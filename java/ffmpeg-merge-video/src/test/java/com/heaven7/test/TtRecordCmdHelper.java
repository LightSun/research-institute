package com.heaven7.test;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TtRecordCmdHelper {

    private static final String TAG = "TtRecordCmdHelper";
    private static final String ENV = "E:\\work\\ai_script";

    public static void main(String[] args) {
      /*  try {
            executeDir("E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
       // genTfRecordAndMov("F:\\videos\\故事线\\婚礼1\\成片");
        genImageTfRecordAndMov("E:\\BaiduNetdiskDownload\\taobao_service\\照片\\男装\\皱麻黑白双层套头衫");
    }
    //for video: mp4
    private static void genTfRecordAndMov(String dir){
        VisitServices.from(FileUtils.getFiles(new File(dir), "mp4"))
                .groupService(new ResultVisitor<String, String>() {
            @Override
            public String visit(String file, Object param) {
                return FileUtils.getFileDir(file, 1, true);
            }
        }).fire(new MapFireVisitor<String, List<String>>() {
            @Override
            public Boolean visit(KeyValuePair<String, List<String>> pair, Object param) {
                try {
                    String dir = pair.getKey();
                    startGenTfrecord(dir, false);
                    moveTfrecords(dir, "tfrecord'");
                } catch (Exception e) {
                   throw new RuntimeException(e);
                }
                return null;
            }
        });
    }
    //for image
    private static void genImageTfRecordAndMov(String dir) {
        String[] formats = {"jpg", "jpeg", "png"};
        final List<String> files = new ArrayList<>();
        File dirFile = new File(dir);
        for (String format : formats) {
            FileUtils.getFiles(dirFile, format, files);
        }
        VisitServices.from(files).groupService(new ResultVisitor<String, String>() {
            @Override
            public String visit(String file, Object param) {
                return FileUtils.getFileDir(file, 1, true);
            }
        }).fire(new MapFireVisitor<String, List<String>>() {
            @Override
            public Boolean visit(KeyValuePair<String, List<String>> pair, Object param) {
                final String realDir = pair.getKey();
                try {
                    startGenTfrecord(realDir, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //move tfrecord
                moveTfrecords(realDir, "tfrecord");
                return null;
            }
        });
    }

    //将生成的tfrecord 移动到视频目录下的tfrecord. and modify name if need
    private static void moveTfrecords(String dir, String extensition) {
        //move to dst
        File tfrecordDir = new File(dir, "tfrecord");
        tfrecordDir.mkdirs();

        VisitServices.from(FileUtils.getFiles(new File(ENV), extensition))
                .fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String path, Object param) {
                String fileName = FileUtils.getFileName(path);
                String dst_suffix = fileName + "." + FileUtils.getFileExtension(path);

                File srcFile = new File(path);
                File dst = new File(tfrecordDir, dst_suffix);
                FileUtils.copyFile(srcFile, dst);
                srcFile.delete();
                Logger.d(TAG, "moveTfrecords", "ok >>> src = " + path + " , dst = " + dst.getAbsolutePath());
                return null;
            }
        });

        //modify tfrecord filename
        ModifyFileName_TTF.modifyFileNameByDir(tfrecordDir.getAbsolutePath(), ModifyFileName_TTF.DELEGATE);
    }

    //generate whole dir of files
    public static void startGenTfrecord(String dir, boolean image) throws Exception {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("/wait");
        cmds.add("python");
        cmds.add(image ? "process_img.py": "process.py");
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
}
