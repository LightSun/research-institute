package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.test.ShotAssigner;
import com.heaven7.ve.utils.Flags;
import com.heaven7.ve.utils.IPreRunDelegate;

/**
 * @author heaven7
 */
public class DebugParam implements IPreRunDelegate{

    /** the default debug param often used for online-env. not for debug */
    public static final DebugParam DEFAULT = new DebugParam();

    public static final int FLAG_ASSIGN_SHOT_TYPE          = 0x0001;
    public static final int FLAG_ASSIGN_SHOT_CUTS          = 0x0002;
    public static final int FLAG_ASSIGN_FACE_COUNT         = 0x0004;
    public static final int FLAG_ASSIGN_BODY_COUNT         = 0x0008;

    public static final int FLAG_ASSIGN_FACE_SCANNER       = 0x0010;
    public static final int FLAG_ASSIGN_HIGH_LIGHT_SCANNER = 0x0020;
    public static final int FLAG_ASSIGN_TAG_SCANNER        = 0x0040;

    private ShotAssigner shotAssigner;
    private MediaResourceScanner faceScanner;
    private MediaResourceScanner highLightScanner;
    private MediaResourceScanner tagScanner;

    private int flags;
    /** the output dir of debug  */
    private String outputDir;
    private String shotsDir;

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

    public MediaResourceScanner getFaceScanner() {
        return faceScanner;
    }
    public void setFaceScanner(MediaResourceScanner faceScanner) {
        this.faceScanner = faceScanner;
    }

    public MediaResourceScanner getHighLightScanner() {
        return highLightScanner;
    }
    public void setHighLightScanner(MediaResourceScanner highLightScanner) {
        this.highLightScanner = highLightScanner;
    }

    public MediaResourceScanner getTagScanner() {
        return tagScanner;
    }
    public void setTagScanner(MediaResourceScanner tagScanner) {
        this.tagScanner = tagScanner;
    }

    public String getShotsDir() {
        return shotsDir;
    }
    public void setShotsDir(String shotsDir) {
        this.shotsDir = shotsDir;
    }

    @Override
    public void prepare(Context param) {
        //resolve wong flag
        if(shotAssigner == null){
            this.flags &= ~(FLAG_ASSIGN_SHOT_TYPE | FLAG_ASSIGN_FACE_COUNT | FLAG_ASSIGN_SHOT_CUTS|FLAG_ASSIGN_BODY_COUNT);
        }
        if(faceScanner == null){
            this.flags &= ~FLAG_ASSIGN_FACE_SCANNER;
        }
        if(tagScanner == null){
            this.flags &= ~FLAG_ASSIGN_TAG_SCANNER;
        }
        if(highLightScanner == null){
            this.flags &= ~FLAG_ASSIGN_HIGH_LIGHT_SCANNER;
        }
    }
}
