package com.heaven7.ve.test;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.Vocabulary;
import com.heaven7.ve.gap.GapManager;

import java.util.ArrayList;
import java.util.List;

/**
 * the cutted item
 */
public class CuttedItem {

    private GapManager.GapItem item;
    private String savePath;

    public GapManager.GapItem getItem() {
        return item;
    }
    public void setItem(GapManager.GapItem item) {
        this.item = item;
    }

    public String getSavePath() {
        return savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getLastModifyTime() {
        MediaPartItem mpi = (MediaPartItem)item.item;
        return mpi.item.getTime();
    }

    public float getTagScore() {
        MediaPartItem mpi = (MediaPartItem)item.item;
        return mpi.getDomainTagScore();
    }

    public Integer getStoryId() {
        MediaPartItem mpi = (MediaPartItem)item.item;
        return mpi.getStoryId();
    }

    public String getPath() {
        MediaPartItem mpi = (MediaPartItem)item.item;
        return mpi.item.getFilePath();
    }

    public String getTagsStr() {
        MediaPartItem mpi = (MediaPartItem)item.item;
        List<List<Integer>> tags = mpi.imageMeta.getTags();
        if(Predicates.isEmpty(tags)){
            return "";
        }
        List<String> tags_str = new ArrayList<>();
        VisitServices.from(tags.get(0)).transformToCollection(new ResultVisitor<Integer, String>() {
            @Override
            public String visit(Integer index, Object param) {
                return Vocabulary.getTagStr(index);
            }
        }).save(tags_str);
        return tags_str.toString();
    }
}
