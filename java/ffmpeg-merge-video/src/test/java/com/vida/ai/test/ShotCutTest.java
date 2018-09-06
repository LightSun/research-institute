package com.vida.ai.test;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.MediaAnalyserImpl;
import com.heaven7.ve.colorgap.impl.TagBasedShotCutter;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.test.TestUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author heaven7
 */
public class ShotCutTest extends BaseMontageTest{

    private static final String TAG = "ShotCutTest";
    private static final String DIR = "F:\\videos\\ClothingWhite\\__cut__";

    private MediaAnalyser mediaAnalyser;
    private TagBasedShotCutter cutter;
    private ColorGapContext mContext;

    public ShotCutTest() {
        super();
        mediaAnalyser = new MediaAnalyserImpl();
        cutter = new TagBasedShotCutter();
        mContext = copySystemResource();
        mContext.setKingdom(KingdomStarter.getKingdom(KingdomStarter.TYPE_DRESS));
        mContext.setColorGapPerformanceCollector(new ColorGapPerformanceCollector());
    }

    @Test //镜头切割差异大的. LM0A0204, LM0A0212, LM0A0224
    public void testCut1(){
        String dir = "F:\\videos\\ClothingWhite";
        String videoName = "LM0A0239.mp4";
        BaseMediaResourceItem item = TestUtils.createVideoItem(dir + File.separator + videoName);
        ArrayList<BaseMediaResourceItem> list = new ArrayList<>(Arrays.asList(item));
        testCutShot(list);
    }

    private List<MediaPartItem> cutItems(List<BaseMediaResourceItem> items, CyclicBarrier barrier) {
        Throwables.checkEmpty(items);
        List<MediaItem> mediaItems = mediaAnalyser.analyse(mContext, items, barrier);
        try {
            barrier.await();
            return cutter.cut(mContext, null, mediaItems);
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] buildCutCmd(MediaPartItem item, String dir){
        return FFmpegUtils.buildCutCmd(item.getVideoPath(), item.videoPart.getStartTime(),
                item.videoPart.getEndTime(), dir);
    }

    private void testCutShot(List<BaseMediaResourceItem> items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteDir(new File(DIR));

                CyclicBarrier barrier = new CyclicBarrier(mediaAnalyser.getAsyncModuleCount() + 1);
                List<MediaPartItem> partItems = cutItems(items, barrier);

                final StringBuilder sb = new StringBuilder();
                for (MediaPartItem item : partItems) {
                    List<List<Integer>> tags = item.getImageMeta().getTags();
                    sb.append("path = ").append(item.getFullPath());
                    if (!Predicates.isEmpty(tags)) {
                        sb.append(" \n,tags = ( ");
                        VisitServices.from(tags.get(0)).fireWithStartEnd(new StartEndVisitor<Integer>() {
                            @Override
                            public boolean visit(Object param, Integer index, boolean start, boolean end) {
                                sb.append(Kingdom.getTagStr(index));
                                if (!end) {
                                    sb.append(", ");
                                }
                                return false;
                            }
                        });
                        sb.append(" )");
                    }
                    sb.append("detail = ").append(item.getDetail()).append(" ,");
                    sb.append("score = ").append(item.getTotalScore());
                    sb.append("\r\n");

                    String[] cmd = buildCutCmd(item, DIR);
                    Logger.d(TAG, "testCutShot", "cmd  = " + Arrays.toString(cmd));
                    // runCmd(() -> new CmdHelper(cmd).execute(new CmdHelper.LogCallback()));
                    new CmdHelper(cmd).execute(new CmdHelper.LogCallback());
                }
                FileUtils.writeTo(new File(DIR,  "tmp_cut_detail.txt"), sb.toString());
                //ffmpeg  -i  F:\\videos\\wedding\\churchIn\\churchIn_C0006.mp4  -vcodec copy  -acodec copy -ss 00:00:25 -to 00:00:30 .cutout.mp4 -y
                // new CmdHelper("");
            }
        }).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
