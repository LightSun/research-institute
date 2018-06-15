package com.heaven7.util;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.colorgap.FrameTags;
import com.heaven7.ve.colorgap.Tag;
import com.heaven7.ve.colorgap.VideoDataLoadUtils;
import com.heaven7.ve.colorgap.Vocabulary;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * 统计工具
 */
public class CountUtils {

    public static StringBuilder buildFrameTagInfos(List<FrameTagInfo> infos, String[] tags, float minPoss) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(tags))
                .append("  minPoss = ")
                .append(minPoss).append("\r\n");
        VisitServices.from(infos).fire(new FireVisitor<FrameTagInfo>() {
            @Override
            public Boolean visit(FrameTagInfo info, Object param) {
                sb.append(info.getMediaPath()).append("\r\n");
                sb.append("        ").append(info.getTimePoints().toString())
                        .append("\r\n");
                return null;
            }
        });
        return sb;
    }

    public static @Nullable FrameTagInfo convertFrameTagInfo(CsvDetail detail, String[] tags, float minPoss){
        final List<Integer> tagIds = VisitServices.from(Arrays.asList(tags)).map(new ResultVisitor<String, Integer>() {
            @Override
            public Integer visit(String s, Object param) {
                return Vocabulary.getTagId(s);
            }
        }).getAsList();
        final List<Integer> timePoints = new ArrayList<>();
        VisitServices.from(detail.getFrames()).queryList(new PredicateVisitor<FrameTags>() {
            @Override
            public Boolean visit(FrameTags frameTags, Object param) {
                List<Tag> topTags = frameTags.getTopTags(Integer.MAX_VALUE, minPoss, Vocabulary.TYPE_WEDDING_ALL);
                VisitServices.from(topTags).fireBatch(new FireBatchVisitor<Tag>() {
                    @Override
                    public Void visit(Collection<Tag> collection, Object param) {
                        for (Tag tag :collection){
                            if(tagIds.contains(tag.getIndex())) {
                                timePoints.add(frameTags.getFrameIdx());
                                break;
                            }
                        }
                        return null;
                    }
                });
                return null;
            }
        });
        if(Predicates.isEmpty(timePoints)){
            return null;
        }
        FrameTagInfo info = new FrameTagInfo();
        info.setDetail(detail);
        info.setTimePoints(timePoints);
        return info;
    }

    public static StringBuilder buildResultText(List<Tag> tags) {
        StringBuilder sb = new StringBuilder();
        VisitServices.from(tags).fire(new FireVisitor<Tag>() {
            @Override
            public Boolean visit(Tag tag, Object param) {
                //\t制表符代表8个空格。不满则补满了。则补8个
                sb.append(String.format("( index, count, mean_possibility, tagName ) = ( \t%d, \t%d, \t%.2f, \t%s)",
                        tag.getIndex(), tag.getExtra(), tag.getPossibility(),tag.getName()))
                        .append("\r\n");
                return null;
            }
        });
        return sb;
    }

    public static void loadData(String dir, VideoDataLoadUtils.LoadCallback callback){
        List<String> tagFiles = FileUtils.getFiles(new File(dir), "csv", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith("_predictions.csv");
            }
        });
        VisitServices.from(tagFiles).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String file, Object param) {
                VideoDataLoadUtils.loadTagData(null, file, callback);
                return null;
            }
        });
    }
    /** merge tags(which index == ) */
    public static List<Tag> merge(List<Tag> allTags){
        return VisitServices.from(allTags).groupService(new ResultVisitor<Tag, Tag>() {
            @Override
            public Tag visit(Tag tag, Object param) {
                return new Tag(tag.getIndex(), 0f);
            }
        }).fire(new MapFireVisitor<Tag, List<Tag>>() {
            @Override
            public Boolean visit(KeyValuePair<Tag, List<Tag>> pair, Object param) {
                int size = pair.getValue().size();
                Tag result = VisitServices.from(pair.getValue()).pile(new PileVisitor<Tag>() {
                    @Override
                    public Tag visit(Object param, Tag tag, Tag tag2) {
                        return new Tag(tag.getIndex(), tag.getPossibility() + tag2.getPossibility());
                    }
                });
                Tag key = pair.getKey();
                key.setPossibility(result.getPossibility() / size);
                key.setName(Vocabulary.getTagStr(key.getIndex()));
                key.setExtra(size);
                return null;
            }
        }).mapKey(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) { //desc
                return Integer.compare((Integer) o2.getExtra(), (Integer) o1.getExtra());
            }
        }).getAsList();
    }
    //startTime. endTime in seconds
    public static List<Tag> getTags(List<FrameTags> tags, int startTime, int endTime , float minPossibility){
        final List<Tag> allTags = new ArrayList<>();
        VisitServices.from(tags)
                .map(new ResultVisitor<FrameTags, List<Tag>>() {
                    @Override
                    public List<Tag> visit(FrameTags frameTags, Object param) {
                        //filter by time.
                        if(frameTags.getFrameIdx() < startTime || frameTags.getFrameIdx() > endTime){
                            return null;
                        }
                        return frameTags.getTopTags(Integer.MAX_VALUE, minPossibility, Vocabulary.TYPE_WEDDING_ALL);
                    }
                }).fire(new FireVisitor<List<Tag>>() {
            @Override
            public Boolean visit(List<Tag> tags, Object param) {
                allTags.addAll(tags);
                return null;
            }
        });
        return allTags;
    }
}
