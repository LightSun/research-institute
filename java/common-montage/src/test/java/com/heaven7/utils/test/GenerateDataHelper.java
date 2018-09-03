package com.heaven7.utils.test;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
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
       // new GenerateDataHelper().start();
        new GenerateDataHelper().genForVideo(DIR + File.separator + "LM0A0199.mp4");
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

    //use temp images
    public void genFaceVideo(String videoPath){
        String fileName = FileUtils.getFileName(videoPath);
        String fileDir = FileUtils.getFileDir(videoPath, 1, true);
        String imageDir = fileDir + File.separator + "temp" + File.separator + fileName;
        String outDir = fileDir + File.separator + fileName;
        File face_rects = new File(outDir, fileName + "_rects.csv");
        FileUtils.createFile(face_rects.getAbsolutePath(), true);

        String[] cmds = FFmpegUtils.buildGetDurationCmd(videoPath);
        CmdHelper.VideoDurationCallback dc = new CmdHelper.VideoDurationCallback();
        new CmdHelper(cmds).execute(dc);
        long duration = dc.getDuration();//in mill seconds

        int count = (int) (duration / 1000) + 1;
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < count ; i ++){
           String file_sn_name =  "img_" + format(i + 1) + ".jpg";
           File file = new File(imageDir, file_sn_name);

        }
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
