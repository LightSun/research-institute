package com.heaven7.ve.colorgap;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextReadHelper;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.Context;

import java.io.File;
import java.util.List;

/**
 * the loader which load rects for image
 *
 * @author heaven7
 */
public class ImageDataLoader {

    /**
     * load rects for images.
     *
     * @param csvPath  the rects file of csv
     * @param imageDir the image dir of batch image. if images are not in same dir or the path in csv use absolute path, this must be null.
     */
    public static List<ImageFaceRects> loadRects(Context context, String csvPath, String imageDir) {
        //pic-012.jpg,0.468057632446289 0.755185484886169 0.114837065339088 0.0766602531075478
        TextReadHelper<ImageFaceRects> helper = new TextReadHelper<>(new ImageFaceRectCallback(csvPath));
        List<ImageFaceRects> list = helper.read(context, csvPath);
        if (!TextUtils.isEmpty(imageDir)) {
            VisitServices.from(list).fire(new FireVisitor<ImageFaceRects>() {
                @Override
                public Boolean visit(ImageFaceRects rects, Object param) {
                    rects.setSrcPath(imageDir + File.separator + rects.getSrcPath());
                    return null;
                }
            });
        }
        return list;
    }

    public static List<ImageTags> loadTags(Context context, String tagPath, String imageDir) {
        TextReadHelper<ImageTags> helper = new TextReadHelper<>(new ImageTagCallback(tagPath));
        List<ImageTags> tags = helper.read(context, tagPath);
        if (!TextUtils.isEmpty(imageDir)) {
            VisitServices.from(tags).fire(new FireVisitor<ImageTags>() {
                @Override
                public Boolean visit(ImageTags imageTags, Object param) {
                    imageTags.setSrcPath(imageDir + File.separator + imageTags.getSrcPath());
                    return null;
                }
            });
        }
        return tags;
    }
    private static class ImageTagCallback extends TextReadHelper.BaseAssetsCallback<ImageTags>{

        private final String csvPath;

        public ImageTagCallback(String csvPath) {
            this.csvPath = csvPath;
        }

        @Override
        public ImageTags parse(String line) {
            if(TextUtils.isEmpty(line)){
                return null;
            }
            String[] strs = line.split(",");
            if(strs.length == 1){
                return ImageTags.DEFAULT;
            }
            ImageTags tags = new ImageTags();
            tags.setSrcPath(FileUtils.decodeChinesePath(strs[0]));
            tags.setTagPath(csvPath);

            FrameTags ft = new FrameTags();
            ft.setFrameIdx(0);
            tags.setTags(ft);
            try {
                // 69 1.000000 67 1.000000 520 0.996915 377 0.976848 220 0.967641 1 0.528127 645 0.400145
                String[] strs2 = strs[1].split(" ");
                for (int i = 0, size = strs2.length; i < size; i += 2) {
                    Tag tag = new Tag(Integer.parseInt(strs2[i]), Float.parseFloat(strs2[i + 1]));
                    if (tag.getPossibility() > -1f) {
                        ft.addTag(tag);
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){
                //ignore
            }
            return tags;
        }
    }
    private static class ImageFaceRectCallback extends TextReadHelper.BaseAssetsCallback<ImageFaceRects> {

        private final String csvPath;

        public ImageFaceRectCallback(String csvPath) {
            this.csvPath = csvPath;
        }

        @Override
        public ImageFaceRects parse(String line) {
            if (TextUtils.isEmpty(line)) {
                return null;
            }
            String[] strs = line.split(",");
            if(strs.length == 1){
                return ImageFaceRects.DEFAULT;
            }
            ImageFaceRects imgRects = new ImageFaceRects();
            imgRects.setRectsPath(csvPath);
            imgRects.setSrcPath(FileUtils.decodeChinesePath(strs[0]));

            FrameFaceRects rects = new FrameFaceRects();
            rects.setFrameIdx(0);
            imgRects.setRects(rects);
            try {
                for (int k = 1, size = strs.length; k < size; k++) {
                    String[] floats = strs[k].split(" ");
                    int len = floats.length;
                    for (int i = 0; i < len; i += 4) {
                        // "0.146632 0.054032 0.096487 0.171532"
                        FaceRect rect = new FaceRect();
                        rect.setX(Float.parseFloat(floats[i]));
                        rect.setY(Float.parseFloat(floats[i + 1]));
                        rect.setWidth(Float.parseFloat(floats[i + 2]));
                        rect.setHeight(Float.parseFloat(floats[i + 3]));
                        rects.addFaceRect(rect);
                    }
                }
            } catch (RuntimeException e) {
                return null;
            }
            return imgRects;
        }
    }

    public static class ImageFaceRects {

        public static final ImageFaceRects DEFAULT = new ImageFaceRects();
        private FrameFaceRects rects;
        /**
         * the image file src path.  for it comes from 'tfrecord', it must be decoded by {@linkplain java.net.URLDecoder}.
         */
        private String srcPath;

        /** the rects path of csv */
        private String rectsPath;

        public String getRectsPath() {
            return rectsPath;
        }
        public void setRectsPath(String rectsPath) {
            this.rectsPath = rectsPath;
        }

        public FrameFaceRects getRects() {
            return rects;
        }
        public void setRects(FrameFaceRects rects) {
            this.rects = rects;
        }

        public String getSrcPath() {
            return srcPath;
        }
        public void setSrcPath(String srcPath) {
            this.srcPath = srcPath;
        }
    }

    public static class ImageTags{

        public static final ImageTags DEFAULT = new ImageTags();

        private FrameTags tags;
        /** for it comes from 'tfrecord', it must be decoded by {@linkplain java.net.URLDecoder}. */
        private String srcPath;
        private String tagPath;

        public String getTagPath() {
            return tagPath;
        }
        public void setTagPath(String tagPath) {
            this.tagPath = tagPath;
        }

        public FrameTags getTags() {
            return tags;
        }
        public void setTags(FrameTags tags) {
            this.tags = tags;
        }

        public String getSrcPath() {
            return srcPath;
        }
        public void setSrcPath(String srcPath) {
            this.srcPath = srcPath;
        }
    }
}
