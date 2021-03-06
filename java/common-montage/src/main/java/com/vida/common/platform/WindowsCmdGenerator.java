package com.vida.common.platform;

import com.heaven7.java.base.anno.Platform;
import com.heaven7.utils.FileUtils;
import com.vida.common.PlatformAICmdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * the windows cmd generator
 * @author heaven7
 */
@Platform
public class WindowsCmdGenerator implements PlatformAICmdGenerator {

    private static final String INFERENCE_PATH = "D:\\tensorflow\\youtube-8m\\inference.py";
    private static final String TRAIN_DIR      = "D:\\tmp\\frame_level_logistic_model";
    private final boolean showWindow;

    public WindowsCmdGenerator(boolean startWindow) {
        this.showWindow = startWindow;
    }

    public static String[] toArray(List<String> list){
        return list.toArray(new String[list.size()]);
    }

    @Override
    public String[] generateTfRecordForVideo(String videoFile, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add(CMD_PROXY_FILENAME);

        String extraCmd = "python video2tfrecord.py %s %s".replaceAll(" ", "+");
        cmds.add(extraCmd);
        cmds.add(PREFIX_INPUT + videoFile);
        cmds.add(PREFIX_OUTPUT + outDir);
        return toArray(cmds);
    }

    @Override
    public String[] generateTfRecordForBatchImages(String inputDir, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add(CMD_PROXY_FILENAME);

        String extraCmd = "python process_img_batch.py %s %s".replaceAll(" ", "+");
        cmds.add(extraCmd);
        cmds.add(PREFIX_INPUT + inputDir);
        cmds.add(PREFIX_OUTPUT + outDir);
        return toArray(cmds);
    }

    @Override
    public String[] generateTfRecordForBatchImages(List<String> images, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add("process_img_batch2.py");
        cmds.add(outDir);
        cmds.addAll(images);
        return toArray(cmds);
    }

    @Override
    public String[] generateFaceForVideo2(String videoFile, String framesDir, String outDir) {
        //py simple-video-filename input-dir outDir img-format bit-count
        String fileName = FileUtils.getFileName(videoFile);
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add("get_video_face_from_dir.py");
        cmds.add(fileName);
        cmds.add(framesDir);
        cmds.add(outDir);
        cmds.add("img_%s.jpg");
        cmds.add(String.valueOf(5));
        return toArray(cmds);
    }

    @Override
    public String[] generateFaceForVideo(String videoFile, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add(CMD_PROXY_FILENAME);

        String extraCmd = "python get_face.py %s %s".replaceAll(" ", "+");
        cmds.add(extraCmd);
        cmds.add(PREFIX_INPUT + videoFile);
        cmds.add(PREFIX_OUTPUT + outDir);
        return toArray(cmds);
    }

    @Override
    public String[] generateFaceForBatchImage(String inputDir, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add(CMD_PROXY_FILENAME);

        String extraCmd = "python get_face_image.py %s %s".replaceAll(" ", "+");
        cmds.add(extraCmd);
        cmds.add(PREFIX_INPUT + inputDir);
        cmds.add(PREFIX_OUTPUT + outDir);
        return toArray(cmds);
    }

    @Override
    public String[] generateFaceForBatchImage(List<String> images, String outDir) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add("get_face_image2.py");
        cmds.add(outDir);
        cmds.addAll(images);
        return toArray(cmds);
    }

    @Override
    public String[] generateTag(String tfrecordFile, String outFile) {
        List<String> cmds = getBaseCmds();
        cmds.add("python");
        cmds.add(CMD_PROXY_FILENAME);
        String extraCmd = getTfRecord2TagCmdTemplate().replaceAll(" ", "+");

        cmds.add(extraCmd);
        cmds.add(PREFIX_INPUT + tfrecordFile);
        cmds.add(PREFIX_OUTPUT + outFile);
        return toArray(cmds);
    }

    @Override
    public String[] generateTag(List<String> tfrecordFile, List<String> outFile) {
        throw new UnsupportedOperationException();
    }

    private String getTfRecord2TagCmdTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append("python ")
                .append(INFERENCE_PATH).append(" ")
                .append("--input_data_pattern=\"%s\" ")
                .append("--output_file=%s ")
                .append("--train_dir=" + TRAIN_DIR).append(" ")
                .append("--frame_features=True ")
                .append("--model=FrameLevelLogisticModel ")
                .append("--feature_names=\"rgb\" ")
                .append("--feature_sizes=\"1024\"");
        return sb.toString();
    }

    private List<String> getBaseCmds() {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("/wait");
        if(!showWindow) {
            cmds.add("/b");
        }
        return cmds;
    }
}
