package com.heaven7.test.baidu;

import com.heaven7.java.image.*;
import com.heaven7.java.image.detect.*;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.test.JavaImageReader;
import com.heaven7.test.JavaMatrix2Transformer;
import com.heaven7.utils.CmdHelper;
import com.vida.common.Platform;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author heaven7
 */
public class ImageDetectorTest {

    final BaiduImageDetector detector = new BaiduImageDetector();
    final JavaImageReader mReader = new JavaImageReader();

    @Test
    public void test1() {
        //test_4 is a merged image
        ImageReader.ImageInfo info = mReader.readBytes("E:\\tmp\\upload_files\\test_4.jpg", "jpg");
        detector.detectKeyPointsBatch(
                BatchInfo.of(4, info.getWidth() / 2, info.getHeight() / 2),
                info.getData(), new ImageDetector.OnDetectCallback<List<KeyPointData>>() {
                    @Override
                    public void onFailed(int code, String msg) {
                        System.out.println(msg);
                    }

                    @Override
                    public void onSuccess(List<KeyPointData> data) {

                    }

                    @Override
                    public void onBatchSuccess(SparseArray<List<KeyPointData>> batchData) {
                        System.out.println(batchData.size());
                    }
                });

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBatch() {
        final Platform platform = Platform.getDefault();
        String imgDir = "E:\\tmp\\upload_files";
        String video = "F:\\videos\\story3\\welcome\\C0012.mp4";
        ImageFactory.setImageInitializer(new ImageInitializer.Builder()
                .setImageDetector(detector)
                .setMatrix2Transformer(new JavaMatrix2Transformer())
                .setImageLimitInfo(new ImageLimitInfo.Builder()
                        .setMaxWidth(1000)
                        .setMaxHeight(1000)
                        .build())
                .setVideoFrameDelegate(new AbstractVideoManager.VideoFrameDelegate() {
                    @Override
                    public byte[] getFrame(String videoFile, int timeInSeconds) {
                        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
                        ImageReader.ImageInfo info = mReader.readBytes(imagePath, "jpg");
                        return info.getData();
                    }

                    @Override
                    public Matrix2<Integer> getFrameMatrix(String videoFile, int timeInSeconds) {
                        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
                        ImageReader.ImageInfo info = mReader.readMatrix(imagePath);
                        return info.getMat();
                    }

                    @Override
                    public ImageReader.ImageInfo getFrame(String videoFile, int timeInSeconds, ImageLimitInfo info) {
                        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
                        return mReader.readBytes(imagePath, "jpg", info);
                    }

                    @Override
                    public ImageReader.ImageInfo getFrameMatrix(String videoFile, int timeInSeconds, ImageLimitInfo info) {
                        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
                        return mReader.readMatrix(imagePath, info);
                    }

                    @Override
                    public int getDuration(String videoFile) {
                        String[] cmds = platform.getFFMpegCmdGenerator(false).buildGetDurationCmd(videoFile);
                        CmdHelper.VideoDurationCallback callback = new CmdHelper.VideoDurationCallback();
                        new CmdHelper(cmds).execute(callback);
                        return (int) (callback.getDuration() / 1000);
                    }
                    @Override
                    public String getFrameImagePath(String videoFile, int time) {
                        //image from 00001. time from 0
                        return imgDir + File.separator + "img_" + format(time + 1) + ".jpg";
                    }
                })
                .build());
        /*VideoHighLightManager bihm = new VideoHighLightManager(video);
        bihm.detectBatch(new AbstractVideoManager.Callback<List<IHighLightData>>() {
            @Override
            public void onCallback(String videoSrc, SparseArray<List<IHighLightData>> dataMap) {
                System.out.println(videoSrc);
            }
        });*/
       VideoKeyPointManager vkpm = new VideoKeyPointManager(video);
       vkpm.detectBatch(new AbstractVideoManager.Callback<List<KeyPointData>>() {
           @Override
           public void onCallback(String videoSrc, SparseArray<List<KeyPointData>> dataMap) {
               System.out.println(videoSrc);
           }
       });
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDuration(){
        String videoFile = "F:\\videos\\story3\\welcome\\C0012.mp4";
        //change showWindow to true. will cause bug when get duration for video.
        String[] cmds = Platform.getDefault().getFFMpegCmdGenerator(false).buildGetDurationCmd(videoFile);
        CmdHelper.VideoDurationCallback callback = new CmdHelper.VideoDurationCallback();
        CmdHelper cmdHelper = new CmdHelper(cmds);
        System.out.println(cmdHelper.getCmdActually());
        cmdHelper.execute(callback);
        System.out.println("duration: " +  callback.getDuration());
    }

    private static String format(int time) {
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
