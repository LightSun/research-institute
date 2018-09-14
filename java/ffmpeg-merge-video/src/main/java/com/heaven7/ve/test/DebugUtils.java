package com.heaven7.ve.test;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.ve.colorgap.MediaPartItem;

import java.util.List;

/**
 * @author heaven7
 */
public class DebugUtils {

    public static void cutShotsToDir(List<MediaPartItem> items, String outDir){
        if(outDir == null){
            return;
        }
        for (MediaPartItem mpi : items){
            if(mpi.isImage()){
                continue;
            }
            String[] cmds = FFmpegUtils.buildCutCmd(mpi.getItem().getFilePath(),
                    mpi.videoPart.getStartTime(), mpi.videoPart.getEndTime(), outDir, null);
            new CmdHelper(cmds).execute(new CmdHelper.InhertIoCallback());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

