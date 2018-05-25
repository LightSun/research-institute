package com.heaven7.ve.test;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.utils.Arrays2;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaAnalyser;
import com.heaven7.ve.colorgap.MediaItem;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.ResourceInitializer;
import com.heaven7.ve.colorgap.impl.MediaAnalyserImpl;
import com.heaven7.ve.colorgap.impl.TagBasedShotCutter;
import com.heaven7.ve.test.util.FFmpegVideoHelper;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CutShotTest extends TestCase{

    private static final String TAG = "CutShotTest";
    public static final String CUT_DIR = "E:\\study\\github\\research-institute\\java\\ffmpeg-merge-video\\cut_videos";

    MediaAnalyser mediaAnalyser;
    TagBasedShotCutter cutter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mediaAnalyser = new MediaAnalyserImpl();
        cutter = new TagBasedShotCutter();
    }

    List<MediaPartItem> cutItems(List<MediaResourceItem> items, CyclicBarrier barrier){
        Throwables.checkEmpty(items);
        ResourceInitializer.init(null);
        List<MediaItem> mediaItems = mediaAnalyser.analyse(null, items, barrier);
        try {
            barrier.await();
            return cutter.cut(null, mediaItems);
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    /**  测试镜头切割 */
    @Test
    public void testCutShot(){
        testCutShot(createItems());
    }

    @Test
    public void testCutShot2(){
        //00:04:22 // 262
        MediaResourceItem item = TestUtils.createVideoItem("F:\\videos\\story2\\church\\C0187.mp4", 262000);
        ArrayList<MediaResourceItem> list = new ArrayList<>(Arrays.asList(item));
        testCutShot(list);
    }

    private void testCutShot(List<MediaResourceItem> items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CyclicBarrier barrier = new CyclicBarrier(2);
                List<MediaPartItem> partItems = cutItems(items, barrier);

                 for(MediaPartItem item : partItems){
                     String[] cmd = FFmpegUtils.buildCutCmd(item, CUT_DIR);
                     Logger.d(TAG,  "testCutShot", "cmd  = " + Arrays.toString(cmd));
                    // runCmd(() -> new CmdHelper(cmd).execute(new CmdHelper.LogCallback()));
                     new CmdHelper(cmd).execute(new CmdHelper.LogCallback());
                 }
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


    private List<MediaResourceItem> createItems() {
        List<MediaResourceItem> items = new ArrayList<>();
       /* String path ="F:\\videos\\test_cut\\test_shot_cut\\concat_output.mp4";
        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);
        item.setTime(new File(path).lastModified());
        item.setDuration(11000);//11s
        item.setMime("video/mp4");*/

        String path ="F:\\videos\\test_cut\\test_shot_cut\\GP5A0859.mp4";
        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);
        item.setTime(new File(path).lastModified());
        item.setDuration(196000);//03:16
        item.setMime("video/mp4");

        items.add(item);
        return items;
    }

    public static void main(String[] args) {
        CutShotTest test = new CutShotTest();
        try {
            test.setUp();
            test.testCutShot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
