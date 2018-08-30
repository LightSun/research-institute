package com.vida.ai.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heaven7.core.util.Logger;
import com.heaven7.java.image.detect.*;
import com.heaven7.java.image.utils.BatchProcessor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.Constants;
import com.heaven7.ve.MediaResourceItem;
import com.vida.common.FFMpegCmdGenerator;
import com.vida.common.ImageExtractCmd;
import com.vida.common.Platform;
import com.vida.common.entity.MediaData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.heaven7.ve.Constants.EXTENSION_IMAGE_HIGH_LIGHT;
import static com.heaven7.ve.Constants.EXTENSION_VIDEO_HIGH_LIGHT;

/**
 * generate highlight info and key point info.
 *
 * @author heaven7
 */
public class ShotInfoGenerator {

    private static final String TAG = "ShotInfoGenerator";
    private static final String RESOLUTION = "1920*1080";

    private final FFMpegCmdGenerator mFFmpegGen = Platform.getDefault().getFFMpegCmdGenerator(false);
    private final ExecutorService mService;
    private final Gson mGson = new GsonBuilder().create();

    public ShotInfoGenerator(ExecutorService mService) {
        this.mService = mService;
    }

    /**
     * gen shot for video
     * @param videoes the videos
     * @param callback the callback
     */
    public void genShotForVideo(List<MediaResourceItem> videoes, Callback callback) {
        Logger.i(TAG, "genShotForVideo", videoes.toString());
        /*
        1, cut video
        2, request high light & get duration
         */
        BatchGenerator bg = new BatchGenerator(callback);
        bg.markStart();
        VisitServices.from(videoes).fire(new FireVisitor<MediaResourceItem>() {
            @Override
            public Boolean visit(MediaResourceItem mid, Object param) {
                final String videoPath = mid.getFilePath();
                String fileDir = FileUtils.getFileDir(mid.getFilePath(), 1, true);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        MediaData mediaData = new MediaData();
                        mediaData.setFilePath(videoPath);
                        //a/b/c.mp4 -> a/b/temp/c/%5d.jpg
                        String saveDir = fileDir + File.separator + Constants.DIR_TEMP
                                + File.separator + FileUtils.getFileName(videoPath);
                        File saveDirF = new File(saveDir);
                        if(!saveDirF.exists() && !saveDirF.mkdirs()){
                            Logger.w(TAG, "genShotForVideo", "mkdirs failed. dir = " + saveDir);
                        }
                        //extract images
                        String[] cmds = mFFmpegGen.buildImageExtractCmd(new ImageExtractCmd.Builder()
                                .setVideoPath(videoPath)
                                .setCountEverySecond(1)
                                .setJpg(true)
                               // .setResolution(RESOLUTION)
                                .setSavePath(saveDir)
                                .build());
                        CmdHelper cmdHelper = new CmdHelper(cmds);
                        cmdHelper.execute();

                        //request highLight
                        Logger.d(TAG, "genShotForVideo", "start request HighLight from video, "
                                + videoPath + " ,saveDir = " + saveDir);
                        VideoHelper helper = new VideoHelper(bg, mediaData);
                        helper.start();
                    }
                };
                bg.addCount(1);
                mService.submit(r);
                return null;
            }
        });
        bg.markEnd();
    }

    /**
     * generate shot info for images
     * @param images the images
     * @param dataDir the data dir of high-light
     * @param callback the callback.
     */
    public void genShotForImage(List<String> images, String dataDir, Callback callback) {
        Logger.i(TAG, "genShotForImage", images.toString());
        BatchImageHighLightManager bihm = new BatchImageHighLightManager(images);
        bihm.detect(new AbstractBatchImageManager.Callback<List<IHighLightData>>() {
            @Override
            public void onCallback(Map<String, List<IHighLightData>> map) {
                VisitServices.from(map).map(new MapResultVisitor<String, List<IHighLightData>, MediaData>() {
                    @Override
                    public MediaData visit(KeyValuePair<String, List<IHighLightData>> pair, Object param) {
                        MediaData md = new MediaData();
                        md.setFilePath(pair.getKey());
                        List<MediaData.HighLightPair> pairs = new ArrayList<>();
                        MediaData.HighLightPair lightPair = new MediaData.HighLightPair();
                        lightPair.setDatas(MediaData.wrapHighLightData(pair.getValue()));
                        pairs.add(lightPair);
                        md.setHighLightDataMap(pairs);
                        return md;
                    }
                }).fire(new FireVisitor<MediaData>() {
                    @Override
                    public Boolean visit(MediaData data, Object param) {
                        String fileName = FileUtils.getFileName(data.getFilePath());
                        String fileDir = FileUtils.getFileDir(data.getFilePath(), 1, true);
                        String targetDir = fileDir + File.separator + Constants.DIR_HIGH_LIGHT;
                        new File(targetDir).mkdirs();
                        String targetFile = targetDir + File.separator + fileName + "." + EXTENSION_IMAGE_HIGH_LIGHT;

                        FileUtils.writeTo(targetFile, mGson.toJson(data));
                        Logger.d(TAG, "genShotForImage", "write high light done. src_image is "
                                + data.getFilePath() +" ,targetFile = " + targetFile);
                        return null;
                    }
                });
                callback.onGenerateDone();
            }
        });
    }

    public interface Callback{
        /**
         * called on generate highlight done
         */
        void onGenerateDone();
    }

    private static class BatchGenerator extends BatchProcessor {

        final Callback callback;
        BatchGenerator(Callback callback) {
            this.callback = callback;
        }
        @Override
        protected void onDone() {
            //for gen there is no data.
            if(callback != null) {
                callback.onGenerateDone();
            }
        }
    }

    private class VideoHelper implements AbstractVideoManager.Callback<List<IHighLightData>>{

        final MediaData mediaData;
        final VideoHighLightManager vhlm;
        final BatchProcessor parentBatcher;

        VideoHelper(BatchProcessor parentBatcher, MediaData mediaData) {
            this.parentBatcher = parentBatcher;
            this.mediaData = mediaData;
            this.vhlm = new VideoHighLightManager(mediaData.getFilePath());
        }

        public void start(){
            vhlm.detectBatch(this);
        }
        @Override
        public void onCallback(String video, SparseArray<List<IHighLightData>> sa) {
            Logger.i(TAG, "onCallback", "VideoHighLight done. for video = " + video + ", size = " + sa.size());
            List<MediaData.HighLightPair> pairs = new ArrayList<>();
            int size = sa.size();
            for (int i = 0; i < size ; i ++){
                int time = sa.keyAt(i);
                List<IHighLightData> data = sa.valueAt(i);
                MediaData.HighLightPair pair = new MediaData.HighLightPair();
                pair.setTime(time);
                pair.setDatas(MediaData.wrapHighLightData(data));
                pairs.add(pair);
            }
            mediaData.setHighLightDataMap(pairs);
            //generate json data and write to file

            String fileName = FileUtils.getFileName(mediaData.getFilePath());
            String fileDir = FileUtils.getFileDir(mediaData.getFilePath(), 1, true);
            //server : data/highlight/xxx.vhighlight
            //local: a/b/c.mp4 -> a/b/highlight/c.vhighlight
            String targetDir = fileDir + File.separator + Constants.DIR_HIGH_LIGHT;
            new File(targetDir).mkdirs();
            String targetFile = targetDir + File.separator + fileName + "." + EXTENSION_VIDEO_HIGH_LIGHT;
            FileUtils.writeTo(targetFile, mGson.toJson(mediaData));
            Logger.i(TAG, "onCallback", "write VideoHighLight done. for video = " + video);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parentBatcher.onTasksEnd(1);
        }
    }



}
