package com.heaven7.utils.test;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.vida.common.Platform;
import com.vida.common.ai.*;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ai generate tests. for video. batch images(same dir or diff dir)
 * @author heaven7
 */
public class BaseAiTest extends TestCase {

    private final AiGenerateContext.OnGenerateListener mListener = new LogOnGenerateListenerImpl();
    protected ExecutorService mService;
    protected AiGeneratorDelegate mAiGenDelegate;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mService.shutdownNow();
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mService = Executors.newFixedThreadPool(5);
      // mAiGenDelegate = new MockAiGeneratorDelegate(new LogOnGenerateListenerImpl());
        mAiGenDelegate = new LocalAiGeneratorDelegateImpl(Platform.getDefault().getAiCmdGenerator(true));
    }

    public void testBatchImageInDiffDir(){
        //chinese path have bugs
        String[] images = {
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2542-1.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2519.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2507.jpg",
        };
        ArrayList<String> list = new ArrayList<>(Arrays.asList(images));
        String output = "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\data2";
        MultiFileParamContextImpl multiFiles = new MultiFileParamContextImpl();
        multiFiles.populateByFiles(list);

        BatchImageAiGenerateContext2 batchImageContext = new BatchImageAiGenerateContext2(mAiGenDelegate, mListener,list, output, multiFiles);
        mService.submit(batchImageContext::genTfRecord);
        mService.submit(batchImageContext::genFace);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void testBatchImageInOneDir(){
        String input = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\蓝灰色无袖长衫";
        String output = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\蓝灰色无袖长衫\\data";
        MultiFileParamContextImpl multiFiles = new MultiFileParamContextImpl();
        multiFiles.populateByDir(input);

        BatchImageAiGenerateContext batchImageContext = new BatchImageAiGenerateContext(mAiGenDelegate, mListener, input, output, multiFiles);
        mService.submit(batchImageContext::genTfRecord);
        mService.submit(batchImageContext::genFace);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testVideo(){
        String videoFile = "F:\\videos\\tmp_store\\1.mp4";
        String dataDir = "F:\\videos\\tmp_store\\data";
        TestFileParamContext context = createVideo("1.mp4");

        VideoAiGenerateContext videoContext = new VideoAiGenerateContext(mAiGenDelegate, mListener, context,
                videoFile, dataDir);

        mService.submit(videoContext::genTfRecord);
        mService.submit(videoContext::genFace);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static TestFileParamContext createVideo(String filePath){
        TestFileParamContext context = new TestFileParamContext();
        context.setSavePath(filePath);
        return context;
    }

    public static class MultiFileParamContextImpl implements MultiFileParamContext {
        final List<FileParamContext> list = new ArrayList<>();
        @Override
        public List<? extends FileParamContext> getAllFileParamContext() {
            return list;
        }
        public void populateByDir(String dir){
            File file = new File(dir);
            List<String> images = new ArrayList<>();
            FileUtils.getFiles(file, "jpg", images);
            FileUtils.getFiles(file, "jpeg", images);
            FileUtils.getFiles(file, "png", images);
            populateByFiles(images);
        }

        public void populateByFiles(List<String> images) {
            List<FileParamContext> list = VisitServices.from(images).map(
                    new ResultVisitor<String, FileParamContext>() {
                        @Override
                        public FileParamContext visit(String filename, Object param) {
                            String fileName = FileUtils.getFileName(filename);
                            String extension = FileUtils.getFileExtension(filename);
                            TestFileParamContext context = new TestFileParamContext();
                            context.setSavePath(fileName + "." + extension);
                            return context;
                        }
                    }).getAsList();
            this.list.addAll(list);
        }
    }
    public static class TestFileParamContext implements FileParamContext{

        private AiVideoFileInfo videoGenInfo;
        private AiBatchImageFileInfo imageGenInfo;
        private String savePath;
        @Override
        public void setVideoGenInfo(AiVideoFileInfo info) {
            videoGenInfo = info;
        }
        @Override
        public void setImageGenInfo(AiBatchImageFileInfo info) {
            imageGenInfo = info;
        }
        @Override
        public String getSavePath() {
            return savePath;
        }
        public AiVideoFileInfo getVideoGenInfo() {
            return videoGenInfo;
        }
        public AiBatchImageFileInfo getImageGenInfo() {
            return imageGenInfo;
        }
        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }
    }
}
