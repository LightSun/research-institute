package com.heaven7.ve.test;

import com.heaven7.core.util.Logger;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.ColorGapManager;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.test.util.FFmpegVideoHelper;
import junit.framework.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试故事线 doing
 */
public class StoreLineTest extends TestCase {

    private static final String TAG = "StoreLineTest";
    // private static final String WEDDING_DIR = "F:\\videos\\wedding";
  /*  private static final String CUT_OUT_WEDDING_DIR =
  "E:\\study\\github\\ffmpeg-merge-video\\cut_videos\\wedding";*/
    private static final String STORY2_DIR = "F:\\videos\\story4";
    private static final String CUT_OUT_WEDDING_DIR =
            "E:\\study\\github\\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story4";

    List<MediaResourceItem> mItems;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    //filter: help we filter files.
    private List<MediaResourceItem> scanWeddingItems(FileFilter filter) {
        List<MediaResourceItem> items = new ArrayList<>();
        File file = new File(STORY2_DIR);
        List<String> videos = new ArrayList<>();
        FileUtils.getFiles(file, "mp4", filter, videos);
        //getVideos(file, filter, videos);

        CmdHelper.VideoDurationCallback durationCallback = new CmdHelper.VideoDurationCallback();
        for (String fileName : videos) {
            Logger.d(TAG, "scanWeddingItems", "video file = " + fileName);
            new CmdHelper(FFmpegUtils.buildGetDurationCmd(fileName)).execute(durationCallback);
            long duration = durationCallback.getDuration();
            //assert duration > 0;
            items.add(TestUtils.createVideoItem(fileName, duration));
        }
        return items;
    }

    public void test1() {
    }

    //bug 183.镜头得分 3.5 = 1.0 + 1 （face）+ 1.5（镜头 ）
    public void testStory() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteDir(new File(CUT_OUT_WEDDING_DIR));
                //scan items
                //mItems = scanWeddingItems(FileUtils.TRUE_FILE_FILTER);
                mItems = scanWeddingItems(FileUtils.ofDirFileFilter("storyTest"));

                ColorGapManager cgm = new ColorGapManager(null,
                                new MediaAnalyserImpl(),
                                new MusicCutterImpl2(45   ),
                                new MusicShaderImpl(),
                                new BasePlaidFiller());
                // 先不设置模版。表示只按照一个章节来测试
               // cgm.setTemplateScriptProvider(new TemplateProviderImpl_ST2());
                cgm.setStoryLineShader(new StoryLineShaderImpl());

                cgm.fill(null, null, mItems, new ColorGapManager.FillCallback() {
                    @Override
                    public void onFillFinished(ColorGapManager.FillResult result) {
                        if(result == null){
                            Logger.w(TAG, "onFillFinished", "fill failed.");
                        }else{
                            List<GapManager.GapItem> gapItems = result.nodes;
//ffmpeg -safe 0 -f concat -i E:\\study\\github\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story2\\concat.txt -c copy concat_output.mp4 -y
                            FFmpegVideoHelper.buildVideo(result.resultTemplate, gapItems, CUT_OUT_WEDDING_DIR);
                            System.out.println("testStory run done...");
                        }
                    }
                });
            }
        };
        new Thread(r).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //can't build right video . why ? may be the ffmpeg is busy.
   // ffmpeg -safe 0 -f concat -i E:\\study\\github\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story4\\concat.txt -c copy E:\\study\\github\\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story4\\merged.mp4 -y
    public void testConcatVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(CUT_OUT_WEDDING_DIR + File.separator + "concat.txt");
                String outVidePath = CUT_OUT_WEDDING_DIR + File.separator + "merged.mp4";
                String[] cmds = FFmpegUtils.buildMergeVideoCmd(file.getAbsolutePath(), outVidePath);
                new CmdHelper(cmds).execute(new CmdHelper.LogCallback());
            }
        }).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
