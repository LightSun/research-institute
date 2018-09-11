package com.heaven7.ve.colorgap;

import com.heaven7.ve.test.ShotAssigner;
import com.heaven7.ve.utils.Flags;

/**
 * @author heaven7
 */
public class DebugParam {

    /** the default debug param often used for online-env. not for debug */
    public static final DebugParam DEFAULT = new DebugParam();

    public static final int FLAG_ASSIGN_SHOT_TYPE  = 0x0001;
    public static final int FLAG_ASSIGN_SHOT_CUTS  = 0x0002;
    public static final int FLAG_ASSIGN_FACE_COUNT = 0x0004;
    public static final int FLAG_ASSIGN_BODY_COUNT = 0x0008;

    private ShotAssigner shotAssigner;
    private int flags;
    /** the output dir of debug  */
    private String outputDir;

    public String getOutputDir() {
        return outputDir;
    }
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setFlags(int flags){
        this.flags = flags;
    }
    public void addFlags(int flags){
        this.flags |= flags;
    }
    public boolean hasFlags(int flags){
        return Flags.hasFlags(this.flags, flags);
    }

    public ShotAssigner getShotAssigner() {
        return shotAssigner;
    }
    public void setShotAssigner(ShotAssigner shotAssigner) {
        this.shotAssigner = shotAssigner;
    }
}
