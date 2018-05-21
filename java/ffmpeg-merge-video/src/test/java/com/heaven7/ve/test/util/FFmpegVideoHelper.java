package com.heaven7.ve.test.util;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.test.CuttedItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * the ffmpeg video helper.
 */
public class FFmpegVideoHelper {

    public static void buildVideo(List<GapManager.GapItem> gapItems, String dir){
        //cut video by cmd
        List<CuttedItem> cutItems = cutVideoActually(gapItems, dir);
        //build concat.txt
        File file = buildConcatFile(cutItems, dir);

        //build merge cmd and execute cmd
        String outVidePath = dir + File.separator + "merged.mp4";
        String[] cmds = FFmpegUtils.buildMergeVideoCmd(file.getAbsolutePath(), outVidePath);
        new CmdHelper(cmds).execute(new CmdHelper.LogCallback());
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
            String[] cmds = FFmpegUtils.buildCutCmd(mpi, dir, outPath);
            new CmdHelper(cmds).execute(new CmdHelper.LogCallback());

            //build cutted item
            CuttedItem ci = new CuttedItem();
            ci.setItem(gapItem);
            ci.setSavePath(outPath[0]);
            cutItems.add(ci);
        }
        return cutItems;
    }
}
