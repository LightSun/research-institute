package com.heaven7.advance;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.utils.FileUtils;
import com.vida.common.ImageExtractCmd;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 视频图像模糊度问题
 */
public class VideoImageBlurTest {

    private static final String SAVE_DIR = "F:\\test\\imgs";
    private static final ImageBlurTest.JavaImageReader READER = new ImageBlurTest.JavaImageReader();

    @Test
    public void extractImages() {
        String videoPath = "F:\\videos\\story0\\churchIn\\C0181.MP4";
        String dst_dir = "F:\\test\\imgs";
        String dir2 = FileUtils.getFileDir(videoPath, 2, false);
        String dir = FileUtils.getFileDir(videoPath, 1, false);
        String targetDir = dst_dir + File.separator + dir2 + File.separator + dir;
        File file = new File(targetDir);
        FileUtils.deleteDir(file);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println(targetDir);

        ImageExtractCmd cmd = new ImageExtractCmd.Builder()
                .setVideoPath(videoPath)
                .setSavePath(targetDir)
                .build();
        String[] cmds = FFmpegUtils.buildImageExtractCmd(cmd, true);
        new CmdHelper(cmds).execute();
    }

    //模糊度
    @Test
    public void testAmbiguityForDir() {
        String videoDir = "F:\\videos\\story0\\dressing";
        List<String> mp4 = FileUtils.getFiles(new File(videoDir), "mp4");
        extractAndGenFiles(mp4);
    }

    @Test
    public void testAmbiguityForFile() {
        String videoPath = "F:\\videos\\story0\\churchIn\\G02.mp4";
        extractAndGenFiles(Arrays.asList(videoPath));
    }
    @Test
    public void testAmbiguityForFile2() {
        String videoPath = "F:\\videos\\story0\\churchIn\\G01.mp4";
        String videoPath2 = "F:\\videos\\story0\\churchIn\\G02.mp4";
        extractAndGenFiles(Arrays.asList(videoPath, videoPath2));
    }

    private void extractAndGenFiles(List<String> mp4) {
        List<String> dirs = new ArrayList<>();
        //extract image
        for (String file : mp4) {
            String dir = getTargetDir(file);
            ImageExtractCmd cmd = new ImageExtractCmd.Builder()
                    .setVideoPath(file)
                    .setSavePath(dir)
                    .build();
            String[] cmds = FFmpegUtils.buildImageExtractCmd(cmd, true);
            new CmdHelper(cmds).execute();
            dirs.add(dir);
        }
        // get blur value
        VisitServices.from(dirs).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String dir, Object param) {
                fire(dir, FileUtils.getFiles(new File(dir), "png"));
                return null;
            }
        });
    }

    private static void fire(String dir, List<String> imageFiles) {
        if(imageFiles.isEmpty()){
            return;
        }
        final StringBuilder sb = new StringBuilder();
        VisitServices.from(imageFiles).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String path, Object param) {
                Matrix2<Integer> mat = READER.read(path);
                ImageBlur blur = new ImageBlur(mat);
                int[] data = blur.laplacian(true, false);
                Matrix2<Integer> mat4 = Matrix2.ofIntArray(blur.getWidth(), blur.getHeight(), data).transpose();
                sb.append(path).append(", blur = ").append(Matrix2Utils.varIntFloat(mat4)).append("\r\n");
                return null;
            }
        });
        File detail = new File(dir, "blurs.txt");
        if(detail.exists()){
            detail.delete();
        }
        FileUtils.writeTo(detail, sb.toString());
    }

    private static String getTargetDir(String videoPath) {
        String dir2 = FileUtils.getFileDir(videoPath, 2, false);
        String dir = FileUtils.getFileDir(videoPath, 1, false);
        String fileName = FileUtils.getFileName(videoPath);
        String targetDir = SAVE_DIR + File.separator + dir2
                + File.separator + dir
                + File.separator + fileName;
        File file = new File(targetDir);
        FileUtils.deleteDir(file);
        if (!file.exists()) {
            file.mkdirs();
        }
        return targetDir;
    }
}
