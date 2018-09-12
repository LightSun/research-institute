package com.heaven7.utils.test;

import com.heaven7.java.base.util.threadpool.Executors2;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.vida.common.Platform;
import com.vida.common.ai.AiGenerateContext;
import com.vida.common.ai.AiGeneratorDelegate;
import com.vida.common.ai.VideoAiGenerateContext;
import com.vida.common.ai.VideoAiGenerateContext2;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.heaven7.utils.test.BaseAiTest.createVideo;

/**
 * @author heaven7
 */
public class GenerateDataHelper {

    private static final String DIR = "F:\\videos\\ClothingWhite";
    private final AiGenerateContext.OnGenerateListener mListener = new LogOnGenerateListenerImpl();
    private final ExecutorService mTagService = Executors2.newSingleThreadExecutor();
    private final ExecutorService mFaceService = Executors2.newFixedThreadPool(3);
    private final AiGeneratorDelegate mAiGenDelegate = new LocalAiGeneratorDelegateImpl(Platform.getDefault().getAiCmdGenerator(true));

    public static void main(String[] args) {
        //renameMp4();
        new GenerateDataHelper().start();
       // new GenerateDataHelper().genForVideo(DIR + File.separator + "LM0A0199.mp4");
    }

    private static void renameMp4() {
        List<String> files = FileUtils.getFiles(new File(DIR), "MP4", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String extension = FileUtils.getFileExtension(pathname);
                if (extension.equalsIgnoreCase("MP4") || extension.equalsIgnoreCase("MOV")) {
                    return true;
                }
                return false;
            }
        });
        FileUtils.getFiles(new File(DIR), "MOV", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String extension = FileUtils.getFileExtension(pathname);
                if (extension.equalsIgnoreCase("MP4") || extension.equalsIgnoreCase("MOV")) {
                    return true;
                }
                return false;
            }
        }, files);
        VisitServices.from(files).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                String fileName = FileUtils.getFileName(s);
                String fileDir = FileUtils.getFileDir(s, 1, true);
                new File(s).renameTo(new File(fileDir, fileName + ".mp4"));
                return null;
            }
        });
    }

    public void start(){
       // FileUtils.getFiles(new File(DIR), "")
        List<String> files = FileUtils.getFiles(new File(DIR), "mp4");
        VisitServices.from(files).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                genForVideo3(s);
                return null;
            }
        });
    }

    public void genForVideo(String videoPath){
        String fileName = FileUtils.getFileName(videoPath);
        String fileDir = FileUtils.getFileDir(videoPath, 1, true);
        String dataDir = fileDir + File.separator + fileName;
        new File(dataDir).mkdirs();
        BaseAiTest.TestFileParamContext context = createVideo(fileName +".mp4");
        VideoAiGenerateContext videoContext = new VideoAiGenerateContext(mAiGenDelegate, mListener, context,
                videoPath, dataDir);

        // mTagService.submit(videoContext::genTfRecord);
        mFaceService.submit(videoContext::genFace);
    }

    //gen face to one dir
    public void genForVideo2(String videoPath){
        String dataDir = "F:\\videos\\ClothingWhite\\faces1";
        new File(dataDir).mkdirs();
        String fileName = FileUtils.getFileName(videoPath);

        BaseAiTest.TestFileParamContext context = createVideo(fileName +".mp4");
        VideoAiGenerateContext videoContext = new VideoAiGenerateContext(mAiGenDelegate, mListener, context,
                videoPath, dataDir);
        mFaceService.submit(videoContext::genFace);
    }
    //gen face to one dir
    public void genForVideo3(String videoPath){
        String dataDir = "F:\\videos\\ClothingWhite\\faces2";
        new File(dataDir).mkdirs();

        String fileName = FileUtils.getFileName(videoPath);
        String fileDir = FileUtils.getFileDir(videoPath, 1, true);
        String framesDir = fileDir + File.separator + "temp" + File.separator + fileName;

        BaseAiTest.TestFileParamContext context = createVideo(fileName +".mp4");
        VideoAiGenerateContext2 videoContext = new VideoAiGenerateContext2(mAiGenDelegate, mListener, context,
                videoPath, dataDir, framesDir);
        mFaceService.submit(videoContext::genFace);
    }

    public static String format(int time) {
        switch (String.valueOf(time).length()) {
            case 1:
                return "0000" + time;

            case 2:
                return "000" + time;
            case 3:
                return "00" + time;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
