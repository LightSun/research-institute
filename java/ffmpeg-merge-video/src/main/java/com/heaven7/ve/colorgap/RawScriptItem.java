package com.heaven7.ve.colorgap;

import java.util.List;

/**
 * the raw script item. like
 * <pre>
 *      String[][] rawScriptTable =
 [["empty","30%",0,"天空",""],
 ["character","1",0,"",""],
 ["churchIn","40%",1,"植物,花艺",""],
 ["dinner","30%",0,"","装饰"]]
 * </pre>
 * one item correspond a logic-sentence. <br><br>
 * Created by heaven7 on 2018/4/11 0011.
 */
@Deprecated
public class RawScriptItem {

    /** the event, often is file dir */
    private String event;

    /** percent of whole plaids (30 means 30%) */
    private int percentage;

    /** the air-shot count */
    private int airShotCount;

    /** 定场镜头tags比较重要 */
    private List<String> firstShotTags;

    /** the important tags */
    private List<String> importantTags;

    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }

    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getAirShotCount() {
        return airShotCount;
    }
    public void setAirShotCount(int airShotCount) {
        this.airShotCount = airShotCount;
    }

    public List<String> getFirstShotTags() {
        return firstShotTags;
    }
    public void setFirstShotTags(List<String> firstShotTags) {
        this.firstShotTags = firstShotTags;
    }

    public List<String> getImportantTags() {
        return importantTags;
    }
    public void setImportantTags(List<String> importantTags) {
        this.importantTags = importantTags;
    }
}
