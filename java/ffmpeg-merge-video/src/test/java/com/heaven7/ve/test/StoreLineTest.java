package com.heaven7.ve.test;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.ColorGapManager;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.test.util.FFmpegVideoHelper;
import junit.framework.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试故事线
 * TODO
 */
public class StoreLineTest extends TestCase{

    private static final String TAG = "StoreLineTest";
    private static final String WEDDING_DIR = "F:\\videos\\wedding";
    private static final String CUT_OUT_WEDDING_DIR = "E:\\study\\github\\ffmpeg-merge-video\\cut_videos\\wedding";

    List<MediaResourceItem> mItems;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mItems = scanWeddingItems();
    }

    private List<MediaResourceItem> scanWeddingItems() {
        List<MediaResourceItem> items = new ArrayList<>();
        File file = new File(WEDDING_DIR);
        List<String> videos = new ArrayList<>();
        getVideos(file, videos);

        CmdHelper.VideoDurationCallback durationCallback = new CmdHelper.VideoDurationCallback();
        for(String fileName : videos){
            Logger.d(TAG , "scanWeddingItems", "video file = "+ fileName);
            new CmdHelper(FFmpegUtils.buildGetDurationCmd(fileName)).execute(durationCallback);
            long duration = durationCallback.getDuration();
            assert duration > 0;
            items.add(TestUtils.createVideoItem(fileName, duration));
        }
        return items;
    }

    private void getVideos(File dir, List<String> outVideos) {
        File[] videoFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(!pathname.isFile()){
                    return false;
                }
                String extension = CommonUtils.getFileExtension(pathname);
                return "mp4".equalsIgnoreCase(extension);
            }
        });
        if(!Predicates.isEmpty(videoFiles)){
            for (File file : videoFiles){
                outVideos.add(file.getAbsolutePath());
            }
        }
        File[] dirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if(!Predicates.isEmpty(dirs)){
            for (File dir1 : dirs){
                getVideos(dir1, outVideos);
            }
        }
    }

    public void test1(){

    }

    public void testStory(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ColorGapManager cgm = new ColorGapManager(null, new MediaAnalyserImpl(),
                        new MusicCutterImpl(), new MusicShaderImpl(), new PlaidFillerImpl());
                cgm.setStoryLineShader(new StoryLineShaderImpl());
                cgm.setTemplateScriptProvider(new TemplateProviderImpl());

                ColorGapManager.FillResult result = cgm.fill(null, null,  mItems);
                List<GapManager.GapItem> gapItems = result.nodes;

                FFmpegVideoHelper.buildVideo(gapItems, CUT_OUT_WEDDING_DIR);
                System.out.println("testStory run done...");
            }
        };
        new Thread(r).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
