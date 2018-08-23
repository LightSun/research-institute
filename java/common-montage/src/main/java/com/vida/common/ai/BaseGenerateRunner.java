package com.vida.common.ai;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGenerateRunner implements Runnable {

    public final String input;
    public final String outPut;

    public BaseGenerateRunner(String[] io) {
        input = io[0];
        outPut = io[1];
    }

    public boolean isVideo() {
        File file = new File(input);
        return file.exists() && file.isFile();
    }

    @Override
    public final void run() {
        File file = new File(outPut);
        if(!file.exists()){
            file.mkdirs();
        }
        onRun(isVideo());
    }

    //called internal
    protected abstract void onRun(boolean video);

    //simpleFileName often is tfrecord or face
    public static void mockBatchImage(String input, String outPut, String simpleFileName, String simpleConfigFilename){
        //create tfs_1534908323496_outputs.tfrecord. and tfs_config.txt
       // File file = new File(outPut, "tfs_" + System.currentTimeMillis() +"_outputs.tfrecord");
        File file = new File(outPut, simpleFileName);
        FileUtils.createFile(file.getAbsolutePath(), true);
        //start build tfs_config.txt
        List<String> images = new ArrayList<>();
        File imageDir = new File(input);
        FileUtils.getFiles(imageDir, "jpg", images);
        FileUtils.getFiles(imageDir, "jpeg", images);
        FileUtils.getFiles(imageDir, "png", images);
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