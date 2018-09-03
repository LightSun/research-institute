package com.heaven7.ve.test.util;

import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;
import com.heaven7.ve.test.CuttedItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * the ffmpeg video helper.
 */
public class FFmpegVideoHelper {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //plaidScatter 格子分布
    public static void buildVideo(VETemplate plaidScatter, List<GapManager.GapItem> gapItems, String dir){
        //cut video by cmd
        List<CuttedItem> cutItems = cutVideoActually(gapItems, dir);
        //build concat.txt
        File file = buildConcatFile(cutItems, dir);
        //for debug, log time
        buildDetail(plaidScatter, cutItems, dir);

        //build merge cmd and execute cmd
        String outVidePath = dir + File.separator + "merged.mp4";
        String[] cmds = FFmpegUtils.buildMergeVideoCmd(file.getAbsolutePath(), outVidePath);
        new CmdHelper(cmds).execute(new CmdHelper.LogCallback());
    }

    private static void buildDetail(VETemplate plaidScatter, List<CuttedItem> cutItems, String dir) {
        File file = new File(dir + File.separator + "concat_files_detail.txt");
        if(file.exists()){
            file.delete();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            final FileWriter nfw = fw;
            //assert plaidScatter.getTotalPlaidCount() == cutItems.size();
           //build chapter items
            List<List<CuttedItem>> chapterItems = new ArrayList<>();
            int lastIndex = 0;
            List<VETemplate.LogicSentence> sentences = plaidScatter.getLogicSentences();
            for(int i = 0 ; i < sentences.size() ; i ++ ){
                VETemplate.LogicSentence ls = sentences.get(i);
                chapterItems.add(cutItems.subList(lastIndex, lastIndex + ls.getPlaidCount()));
                lastIndex += ls.getPlaidCount();
            }
            //每个story
            int chapterIndex = 0;
            for(List<CuttedItem> chapter : chapterItems){
                fw.write("============ Chapter: " + chapterIndex + " ============ \r\n");
                VisitServices.from(chapter).groupService(new ResultVisitor<CuttedItem, Integer>() {
                    @Override
                    public Integer visit(CuttedItem item, Object param) {
                        return item.getStoryId();
                    }
                }).fire(new MapFireVisitor<Integer, List<CuttedItem>>() {
                    @Override
                    public Boolean visit(KeyValuePair<Integer, List<CuttedItem>> pair, Object param) {
                        Integer key = pair.getKey();
                        try {
                            // nfw.write("Chapter " + key + ":\r\n");
                            nfw.write("Story " + key + " files :\r\n");
                            nfw.write("     ");
                            for (CuttedItem item : pair.getValue()){
                                nfw.write(FileUtils.getFileName(item.getPath()) + "(" + item.getTagsStr() +  ")" + ", ");
                            }
                            nfw.write("\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
                chapterIndex ++;
            }
            nfw.write("\r\n");

            for (CuttedItem item : cutItems){
                //file 'F:\\videos\\wedding\\empty\\empty_C0015.mp4'
                fw.write("file '"+ item.getSavePath() + "'  tagScore = "+ item.getTagScore() + " ,bias = " + item.isBiasShot()
                        + " ,time = "  + SDF.format(item.getLastModifyTime()) + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static File buildConcatFile(List<CuttedItem> cutItems, String dir) {
        File file = new File(dir + File.separator + "concat.txt");
        if(file.exists()){
            file.delete();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            for (CuttedItem item : cutItems){
                //file 'F:\\videos\\wedding\\empty\\empty_C0015.mp4'
                fw.write("file '"+ item.getSavePath() + "'\n" );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }

    private static List<CuttedItem> cutVideoActually(List<GapManager.GapItem> gapItems, String dir) {
        List<CuttedItem> cutItems = new ArrayList<>();
        String[] outPath = new String[1];
        for (GapManager.GapItem gapItem : gapItems){
            MediaPartItem mpi = (MediaPartItem) gapItem.item;
            String[] cmds = FFmpegUtils.buildCutCmd(mpi.getItem().getFilePath(),
                    mpi.videoPart.getStartTime(), mpi.videoPart.getEndTime(),dir, outPath);
            new CmdHelper(cmds).execute();
            //sleep for some case . cut video not done.
           /* try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //build cutted item
            CuttedItem ci = new CuttedItem();
            ci.setItem(gapItem);
            ci.setSavePath(outPath[0]);
            cutItems.add(ci);
        }
        return cutItems;
    }
}
