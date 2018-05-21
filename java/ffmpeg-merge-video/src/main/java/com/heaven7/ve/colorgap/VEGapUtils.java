package com.heaven7.ve.colorgap;


import com.heaven7.ve.gap.ItemDelegate;
import com.heaven7.ve.gap.PlaidDelegate;

import java.io.File;
import java.util.List;

/**
 * Created by heaven7 on 2018/5/15 0015.
 */

public class VEGapUtils {

    /**
     * 视频文件的关键目录 为2级目录模式,比如:
     empty/dinner/xxx.mp4 , empty/white/xxx2.mp4.
     那么empty就是文件的关键目录
     */
    public static String getFileDir(String filepath, boolean fullPath){
        File file = new File(filepath);
        if(file.exists() && file.isFile()){
            File parent = file.getParentFile();
            if(parent != null && parent.isDirectory()){
               /* String dir = parent.getName();
                if(MetaInfo.DIR_EMPTY.equals(dir)){
                    return fullPath ? parent.getAbsolutePath() :MetaInfo.DIR_EMPTY;
                }*/
                parent = parent.getParentFile();
                if(parent != null && parent.isDirectory()){
                    return fullPath ? parent.getAbsolutePath() :parent.getName();
                }
               //return fullPath ? parent.getAbsolutePath() : dir;
            }
        }
        return null;
    }
    /**
     * 视频文件的关键目录 为2级目录模式,比如:
     empty/dinner/xxx.mp4 , empty/white/xxx2.mp4.
     那么empty就是文件的关键目录
     */
    public static String getFileDir(String filepath, int depth, boolean fullPath){
        if(depth < 1) throw new IllegalArgumentException("depth must > 0");
        File file = new File(filepath);
        if(file.exists() && file.isFile()){
            File parent = file;
            while (depth > 0){
                depth --;
                parent = parent.getParentFile();
                if(parent == null || !parent.isDirectory()){
                    throw new IllegalStateException("file path is wrong or depth is wrong");
                }
            }
            return fullPath ? parent.getAbsolutePath() :parent.getName();
        }
        return null;
    }

    public static <Item extends ItemDelegate> Item filterByScore(PlaidDelegate plaid, List<Item> items){
        //check hold until find , if not found , not check hold
        Item item = filterByScore0(plaid, items, true);
        if(item != null){
            return item;
        }
        item = filterByScore0(plaid, items, false);
        return item;
    }
    private static <Item extends ItemDelegate> Item filterByScore0(PlaidDelegate plaid, List<Item> items, boolean checkHold){
        float maxScore = -1f;
        Item bestItem = null;
        for(Item item : items){
            if(checkHold && item.isHold()){
                continue;
            }
            // int score = filter.computeScoreWithWeight(mpi.getColorCondition());
            float filterScore = plaid.computeScore(item.getColorCondition());
            float tagScore = item.getDomainTagScore();
            float score = filterScore + tagScore;
            if(score > maxScore){
                maxScore = score;
                bestItem = item;
            }
        }
        return bestItem;
    }
}
