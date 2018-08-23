package com.vida.common.ai;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextReadHelper;
import com.vida.common.platform.PyDelegate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author heaven7
 */
public abstract class BaseMockAiGeneratorDelegate extends BaseAiGeneratorDelegate{

    private static final String TAG = "BaseMockAiGeneratorDele";

    private final long tagDuration;
    private final long faceDuration;

    public BaseMockAiGeneratorDelegate() {
        this(2000L, 4000L);
    }
    public BaseMockAiGeneratorDelegate(long tagDuration, long faceDuration) {
        this.tagDuration = tagDuration;
        this.faceDuration = faceDuration;
    }

    @Override
    protected String[] getGenTfRecordCmd(String[] io) {
        return new String[]{"just mock [getGenTfRecordCmd]"};
    }

    @Override
    protected String[] getGenFaceCmd(String[] io) {
        return new String[]{"just mock [getGenFaceCmd]"};
    }
    @Override
    protected String[] getGenTagCmd(String[] io, String tfrecordPath) {
        return new String[]{"just mock [getGenTagCmd]"};
    }
    @Override
    protected void genTfrecord(String[] io, String[] cmds) {
        String msg = formatIo(io);
        mockImpl(tagDuration / 2, "genTfrecord" ,msg, new MockTfRecordRunner(io));
    }

    @Override
    protected void genTag(String[] io, String[] cmds) {
        String msg = formatIo(io);
        mockImpl(tagDuration / 2, "genTag",msg, new MockTagRunner(io));
    }
    @Override
    protected void genFace(String[] io, String[] cmds) {
        String msg = formatIo(io);
        mockImpl(faceDuration, "genFace",msg, new MockFaceRunner(io));
    }

    private void mockImpl(long mockDuration, String method ,String msg, Runnable r){
        Logger.d(TAG, method, "Start --- "  + msg);
        r.run();
        try {
            Thread.sleep(mockDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logger.d(TAG, method, "End --- "  + msg);
    }

    private static String formatIo(String[] io) {
        return String.format("input is %s, output is %s.", io[0], io[1]);
    }

    private static class MockTfRecordRunner extends BaseGenerateRunner{

        MockTfRecordRunner(String[] io){
            super(io);
        }

        @Override
        public void onRun(boolean isVideo) {
            if(isVideo){
                //create file xxx_output.tfrecord
                String fileName = FileUtils.getFileName(getInput().get(0));
                File file = new File(getOutput(), fileName +"_output.tfrecord");
                FileUtils.createFile(file.getAbsolutePath(), true);
            }else{
                //create tfs_1534908323496_outputs.tfrecord. and tfs_config.txt
                if(type == TYPE_BATCH_IMAGE_MORE_DIR){
                    mockBatchImage(getInput(), getOutput(), "tfs_" + System.currentTimeMillis() +"_outputs.tfrecord",
                            "tfs_config.txt");
                }else{
                    mockBatchImage(getInput().get(0), getOutput(), "tfs_" + System.currentTimeMillis() +"_outputs.tfrecord",
                            "tfs_config.txt");
                }
            }
        }
    }
    private static class MockTagRunner extends BaseGenerateRunner{

        public MockTagRunner(String[] io) {
            super(io);
        }
        @Override
        protected void onRun(boolean isVideo) {
            if(isVideo){
                //create file xxx_output.tfrecord
                String fileName = FileUtils.getFileName(getInput().get(0));
                File file = new File(getOutput(), fileName + "_predictions.csv");
                FileUtils.createFile(file.getAbsolutePath(), true);
            }else{
                File file_config = new File(getOutput(), "tfs_config.txt");
                List<TfsConfigLine> list = new TextReadHelper<TfsConfigLine>(new TextReadHelper.Callback<TfsConfigLine>() {
                    @Override
                    public BufferedReader open(Context context, String url) throws IOException {
                        return new BufferedReader(new FileReader(url));
                    }
                    @Override
                    public TfsConfigLine parse(String line) {
                        return new TfsConfigLine(line);
                    }
                }).read(null, file_config.getAbsolutePath());
                if(!Predicates.isEmpty(list)){
                    TfsConfigLine line = list.get(0);
                    String tagPath = PyDelegate.getDefault().getTagOutputFile(line.tfrecordPath);
                    FileUtils.createFile(tagPath, true);
                }
            }
        }
    }
    private static class MockFaceRunner extends BaseGenerateRunner{

        public MockFaceRunner(String[] io) {
            super(io);
        }
        @Override
        protected void onRun(boolean isVideo) {
            if(isVideo){
                //create file xxx_output.tfrecord
                String fileName = FileUtils.getFileName(getInput().get(0));
                File file = new File(getOutput(), fileName + "_rects.csv");
                FileUtils.createFile(file.getAbsolutePath(), true);
            }else{
                if(type == TYPE_BATCH_IMAGE_MORE_DIR){
                    mockBatchImage(getInput(), getOutput(), "face_" + System.currentTimeMillis() +"_rects.csv",
                            "face_config.txt");
                }else{
                    mockBatchImage(getInput().get(0), getOutput(), "face_" + System.currentTimeMillis() +"_rects.csv",
                            "face_config.txt");
                }
            }
        }
    }
    private static class TfsConfigLine{
        final String tfrecordPath;
        final List<String> mImages = new ArrayList<>(6);

        public TfsConfigLine(String line) {
            String[] strs = line.split(",");
            tfrecordPath = strs[0];
            if(strs.length > 1){
                String[] images = strs[1].split(" ");
                this.mImages.addAll(Arrays.asList(images));
            }
        }
    }
    /*
    video:
    1534919479475_output.tfrecord
    1534919479475_predictions.csv
    1534919479475_rects.csv
     *
     */
    /*
     * image:
     * face_1534908322981_rects.csv
     * face_config.txt
     * tfs_1534908323496_outputs.tfrecord
     * tfs_1534908323496_predictions.csv
     * tfs_config.txt
     *
     * face_config.txt:
     *       xxx/xxx/face_1534908322981_rects.csv, xxx/xxx.jpg xxx/xxx.jpg
     * tfs_config_txt:
     *       xxx/xxx/tfs_1534908323496_outputs.tfrecord, xxx/xxx.jpg xxx/xxx.jpg
     */

}
