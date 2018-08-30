package com.heaven7.utils.test;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.vida.common.Platform;
import com.vida.common.ai.AiGenerateContext;
import com.vida.common.ai.AiGeneratorDelegate;
import com.vida.common.ai.VideoAiGenerateContext;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heaven7.utils.test.BaseAiTest.createVideo;

/**
 * @author heaven7
 */
public class GenerateDataHelper {

    private static final String DIR = "F:\\videos\\ClothingWhite";
    private final AiGenerateContext.OnGenerateListener mListener = new LogOnGenerateListenerImpl();
    private final ExecutorService mTagService = Executors.newSingleThreadExecutor();
    private final ExecutorService mFaceService = Executors.newFixedThreadPool(3);
    private final AiGeneratorDelegate mAiGenDelegate = new LocalAiGeneratorDelegateImpl(Platform.getDefault().getAiCmdGenerator(true));

    public static void main(String[] args) {
        //renameMp4();
        new GenerateDataHelper().start();
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
                genForVideo(s);
                return null;
            }
        });
    }

    private void genForVideo(String videoPath){
        String fileName = FileUtils.getFileName(videoPath);
        String fileDir = FileUtils.getFileDir(videoPath, 1, true);
        String dataDir = fileDir + File.separator + fileName;
        new File(dataDir).mkdirs();
        BaseAiTest.TestFileParamContext context = createVideo(fileName +".mp4");
        VideoAiGenerateContext videoContext = new VideoAiGenerateContext(mAiGenDelegate, mListener, context,
                videoPath, dataDir);

        mTagService.submit(videoContext::genTfRecord);
      //  mFaceService.submit(videoContext::genFace);
    }

}
