package com.heaven7.ve.colorgap;

/**
 * 字典中单个tag的信息
 * Created by heaven7 on 2018/4/11 0011.
 */

public class Tag {

    private int index;         //id索引
    private float possibility; //概率
    private String name;
    private String wikiUrl;
    //vertical1

    public Tag(int index, float possibility) {
        this.index = index;
        this.possibility = possibility;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public float getPossibility() {
        return possibility;
    }
    public void setPossibility(float possibility) {
        this.possibility = possibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }
}
