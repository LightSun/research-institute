package com.vida.ai.data.transform;

import com.google.gson.Gson;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.Constants;
import com.vida.common.IOUtils;
import com.vida.common.entity.MediaData;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * transform ios high-light data to normal high-light data.
 * Created by heaven7 on 2018/9/9.
 */
public class Ios2Me_HighLight {

    public static void main(String[] args) {
        String inputDir = "E:\\tmp\\ChenJun\\DomainTags\\clothing";
        String outDir = "F:\\videos\\ClothingWhite\\highlight";
        final Ios2Me_HighLight light = new Ios2Me_HighLight(inputDir, outDir);
        light.setImageWidth(1280);
        light.setImageHeight(720);
        light.transform();
    }

    private final String inputDir;
    private final String outDir;
    int imageWidth;
    int imageHeight;

    public Ios2Me_HighLight(String inputDir, String outDir) {
        this.inputDir = inputDir;
        this.outDir = outDir;
    }

    public void transform(){
        final List<String> files = FileUtils.getFiles(new File(inputDir), new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
        final Gson gson = new Gson();
        VisitServices.from(files).map(new ResultVisitor<String, DataWrapper>() {
            @Override
            public DataWrapper visit(String s, Object param) {
                final String fileName = getFileName(s);
                final String[] strs = fileName.split("_");
                String filename = strs[0];
                final String json = IOUtils.readFileAsString(s);
                final IosHighLightData data = gson.fromJson(json, IosHighLightData.class);
                return new DataWrapper(filename, data);
            }
        }).asListService().groupService(new ResultVisitor<DataWrapper, String>() {
            @Override
            public String visit(DataWrapper dataWrapper, Object param) {
                return dataWrapper.getFilename();
            }
        }).map(new MapResultVisitor<String, List<DataWrapper>, MediaData>() {
            @Override
            public MediaData visit(KeyValuePair<String, List<DataWrapper>> t, Object param) {
                DataWrapper wrapper = merge(t.getValue());
                if(wrapper == null){
                    return null;
                }
                return transformImpl(wrapper);
            }
        }).fire(new FireVisitor<MediaData>() {
            @Override
            public Boolean visit(MediaData mediaData, Object param) {
                final List<MediaData.HighLightPair> pairs = VisitServices.from(
                        mediaData.getHighLightDataMap()).filter(
                        new PredicateVisitor<MediaData.HighLightPair>() {
                            @Override
                            public Boolean visit(MediaData.HighLightPair pair, Object param) {
                                return !Predicates.isEmpty(pair.getDatas());
                            }
                        }).getAsList();
                mediaData.setHighLightDataMap(pairs);
                final String filename = mediaData.getFilePath();
                final String content = gson.toJson(mediaData);
                //LM0A0212.vhighlight
                String targetFile = outDir + File.separator + filename
                        +"."+ Constants.EXTENSION_VIDEO_HIGH_LIGHT;
                FileUtils.writeTo(targetFile, content);
                return null;
            }
        });
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    private DataWrapper merge(List<DataWrapper> value) {
        if(Predicates.isEmpty(value)){
            return null;
        }
        return VisitServices.from(value).pile(new PileVisitor<DataWrapper>() {
            @Override
            public DataWrapper visit(Object o, DataWrapper dw1, DataWrapper dw2) {
                final List<IosHighLightData.DomainTagData> data = dw1.getData().getDomainTagData();
                data.addAll(dw2.getData().getDomainTagData());
                return dw1;
            }
        });
    }
    private MediaData transformImpl(DataWrapper data) {
        MediaData md = new MediaData();
        md.setFilePath(data.getFilename());
        final List<MediaData.HighLightPair> list = VisitServices.from(
                data.getData().getDomainTagData())
                .map(new ResultVisitor<IosHighLightData.DomainTagData, MediaData.HighLightPair>() {
                    @Override
                    public MediaData.HighLightPair visit(IosHighLightData.DomainTagData dtd, Object param) {
                       /* final List<Integer> frameSize = dtd.getFrameSize();
                        int frameWidth = frameSize.get(0);
                        int frameHeight = frameSize.get(1);*/
                        MediaData.HighLightPair pair = new MediaData.HighLightPair();
                        pair.setTime(dtd.getTime());
                        pair.setDatas(VisitServices.from(dtd.getDatas()).map(
                                new ResultVisitor<IosHighLightData.Data, MediaData.HighLightData>() {
                            @Override
                            public MediaData.HighLightData visit(IosHighLightData.Data data, Object param) {
                                final Location location = new Location();
                                location.left = (int) (data.getX() * imageWidth);
                                location.top = (int) (data.getY() * imageHeight);
                                location.width = (int) (data.getWidth() * imageWidth);
                                location.height = (int) (data.getHeight() * imageHeight);
                                MediaData.HighLightData  hld = new MediaData.HighLightData();
                                hld.setName(data.getName());
                                hld.setScore(data.getConfidence());
                                hld.setLocation(location);
                                return hld;
                            }
                        }).getAsList());
                        return pair;
                    }
                }).getAsList();
        md.setHighLightDataMap(list);
        return md;
    }
    private static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            index = path.lastIndexOf("\\");
        }
        return path.substring(index + 1);
    }
}
