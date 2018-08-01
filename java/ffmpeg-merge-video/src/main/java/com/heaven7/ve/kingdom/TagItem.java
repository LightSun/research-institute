package com.heaven7.ve.kingdom;

/**
 * the tag item of word
 */
public class TagItem {
    private int index;
    private String name;
    private double score;
    private String shotType1;
    private String shotType2;
    private String desc;
    private Float shotTypeScore; //for wedding 3 or 1

    public TagItem() {
    }

    public TagItem(int index, String name, double score, String shotType1, String shotType2) {
        this(index, name, score, shotType1, shotType2, null);
    }

    public TagItem(int index, String name, double score, String shotType1, String shotType2, String desc) {
        this.index = index;
        this.name = name;
        this.score = score;
        this.shotType1 = shotType1;
        this.shotType2 = shotType2;
        this.desc = desc;
    }

    public TagItem(TagItem item, String desc) {
        this(item.index, item.name, item.score, item.shotType1, item.shotType2, desc);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getShotType1() {
        return shotType1;
    }

    public void setShotType1(String shotType1) {
        this.shotType1 = shotType1;
    }

    public String getShotType2() {
        return shotType2;
    }

    public void setShotType2(String shotType2) {
        this.shotType2 = shotType2;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getShotTypeScore() {
        return shotTypeScore;
    }
    public void setShotTypeScore(Float shotTypeScore) {
        this.shotTypeScore = shotTypeScore;
    }
}