package com.heaven7.ve.kingdom;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * the module data. which comes from ai-train
 * @author heaven7
 */
public class ModuleData {

    private String name;
    private float score;
    private String shotType;
    private int level; //used for proportion
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getShotType() {
        return shotType;
    }

    public void setShotType(String shotType) {
        this.shotType = shotType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static void main(String[] args) {
        List<ModuleData> list = new ArrayList<>();
        populate(list);
        KingdomData data = new KingdomData();
        data.setModuleDatas(list);
        System.out.println(new Gson().toJson(data));
    }

    private static void populate(List<ModuleData> list) {
        ModuleData data;
        data = new ModuleData();
        data.setName("printing");
        data.setDesc("服装印花");
        data.setLevel(2);
        data.setScore(0.2f);
        data.setShotType("closeUp");
        list.add(data);

        data = new ModuleData();
        data.setName("zipper");
        data.setDesc("纽扣拉链");
        data.setLevel(2);
        data.setScore(0.2f);
        data.setShotType("closeUp");
        list.add(data);

        data = new ModuleData();
        data.setName("selling_point");
        data.setDesc("特⾊买点");
        data.setLevel(2);
        data.setScore(0.2f);
        data.setShotType("closeUp");
        list.add(data);

        data = new ModuleData();
        data.setName("Portrait");
        data.setDesc("肖像");
        data.setLevel(0);
        data.setScore(1.5f);
        data.setShotType("mediumShot");
        list.add(data);

        data = new ModuleData();
        data.setName("back");
        data.setDesc("背影");
        data.setLevel(0);
        data.setScore(1.5f);
        data.setShotType("mediumLongShot");
        list.add(data);

        data = new ModuleData();
        data.setName("foot");
        data.setDesc("腿部");
        data.setLevel(1);
        data.setScore(0.2f);
        data.setShotType("closeUp");
        list.add(data);

        data = new ModuleData();
        data.setName("hand");
        data.setDesc("手部");
        data.setLevel(1);
        data.setScore(0.2f);
        data.setShotType("closeUp");
        list.add(data);
    }
}
