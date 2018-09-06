package com.heaven7.ve.colorgap;

/**
 * the parameter of ai-montage.
 * @author heaven7
 */
public class MontageParam {

    private String effectFileName;
    private String templateFileName;
    /** duration in seconds */
    private int duration;

    /** get the duration in seconds */
    public int getDuration() {
        return duration;
    }

    /**
     * set the duration
     * @param duration the target duration in seconds.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEffectFileName() {
        return effectFileName;
    }
    public void setEffectFileName(String effectFileName) {
        this.effectFileName = effectFileName;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }
    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }
}
