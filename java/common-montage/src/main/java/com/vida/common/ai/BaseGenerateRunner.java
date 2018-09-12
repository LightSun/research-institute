package com.vida.common.ai;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseGenerateRunner implements Runnable {

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_BATCH_IMAGE_ONE_DIR = 2;
    public static final int TYPE_BATCH_IMAGE_MORE_DIR = 3;

    public final String[] io;
    public int type;

    public BaseGenerateRunner(String[] io) {
        this.io = io;
        if(isVideo()){
            type = TYPE_VIDEO;
        }else{
            File file = new File(io[0]);
            File file2 = new File(io[1]);
            //dir f1 f2 f3...
            if(file2.isFile()){
                type = TYPE_BATCH_IMAGE_MORE_DIR;
                file.mkdirs();
            }else{
                //input-dir output-dir
                type = TYPE_BATCH_IMAGE_ONE_DIR;
                file2.mkdirs();
            }
        }
    }

    public List<String> getInput(){
        switch (type){
            case TYPE_VIDEO:
                return Arrays.asList(io[0]);

            case TYPE_BATCH_IMAGE_MORE_DIR:
                ArrayList<String> list = new ArrayList<>(Arrays.asList(io));
                return list.subList(1, list.size());

            case TYPE_BATCH_IMAGE_ONE_DIR:
                return Arrays.asList(io[0]);

            default:
                throw new IllegalStateException("wrong type = " + type);
        }
    }
    public String getOutput(){
        switch (type){
            case TYPE_VIDEO:
            case TYPE_BATCH_IMAGE_ONE_DIR:
                return io[1];

            case TYPE_BATCH_IMAGE_MORE_DIR:
                return io[0];

            default:
                throw new IllegalStateException("wrong type = " + type);
        }
    }

    public String getParameter(int index){
         if(io.length > index){
             return io[index];
         }
         return null;
    }

    public boolean isVideo() {
        File file = new File(io[0]);
        return file.exists() && file.isFile();
    }

    @Override
    public final void run() {
        onRun(isVideo());
    }

    //called internal
    protected abstract void onRun(boolean video);

    //simpleFileName often is tfrecord or face
    public static void mockBatchImage(String input, String outPut, String simpleFileName, String simpleConfigFilename){
        List<String> images = new ArrayList<>();
        File imageDir = new File(input);
        FileUtils.getFiles(imageDir, "jpg", images);
        FileUtils.getFiles(imageDir, "jpeg", images);
        FileUtils.getFiles(imageDir, "png", images);
        mockBatchImage(images, outPut, simpleFileName, simpleConfigFilename);
    }
    /** batch image for different dir */
    public static void mockBatchImage(List<String> images, String outPut, String simpleFileName, String simpleConfigFilename){
        //create tfs_1534908323496_outputs.tfrecord. and tfs_config.txt
        // File file = new File(outPut, "tfs_" + System.currentTimeMillis() +"_outputs.tfrecord");
        File file = new File(outPut, simpleFileName);
        FileUtils.createFile(file.getAbsolutePath(), true);
        //start build tfs_config.txt
        //build content
        final StringBuilder sb = new StringBuilder();
        sb.append(file.getAbsolutePath());
        if(!Predicates.isEmpty(images)){
            VisitServices.from(images).fireWithStartEnd(new StartEndVisitor<String>() {
                @Override
                public boolean visit(Object param, String s, boolean start, boolean end) {
                    if(start){
                        sb.append(",").append(s);
                    }else{
                        sb.append(" ").append(s);
                    }
                    return false;
                }
            });
        }
        //write tfs_config.txt
        //File file_config = new File(outPut, "tfs_config.txt");
        File file_config = new File(outPut, simpleConfigFilename);
        FileUtils.writeTo(file_config, sb.toString());
    }
}