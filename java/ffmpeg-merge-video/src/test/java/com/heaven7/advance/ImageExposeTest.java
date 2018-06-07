package com.heaven7.advance;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.test.util.FileHelper;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图像曝光度. https://blog.csdn.net/qq249356520/article/details/78864244
 * 模糊图像识别有影响
 */
public class ImageExposeTest {

    private static final int STEP = 8;
    public static final PileVisitor<Float> FLOAT_PILE = (o, aFloat, aFloat2) -> aFloat + aFloat2;


    public static void main(String[] args) {
     /*   Color c = Color.BLACK;
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        System.out.println(hsb[2]);//0.0*/

       // String dir = "F:\\test\\imgs_dark"; //dark
        String dir = "F:\\test\\img_expose";
        List<String> files = new ArrayList<>();
        FileHelper.getFiles(new File(dir), "jpg", files);
        for(String darkPath : files){
            Matrix2<Float> matrix = new Matrix2<>(getImageBrightness(darkPath));
            List<Matrix2<Float>> list = matrix.divideChunk(STEP, STEP);

            final AtomicInteger overDark = new AtomicInteger();
            final AtomicInteger overExpose = new AtomicInteger();
            final StringBuilder sb = new StringBuilder();
            sb.append("path = ").append(darkPath).append("\r\n");
            sb.append("means = (");
            VisitServices.from(list).fireWithStartEnd(new StartEndVisitor<Matrix2<Float>>() {
                int size;
                @Override
                public boolean visit(Object param, Matrix2<Float> mat, boolean start, boolean end) {
                    float mean = mat.sum(FLOAT_PILE) / mat.getTotalSize();
                    sb.append(mean);
                    if(mean < 0.175f){
                        overDark.addAndGet(1);
                    }
                    if(mean > 0.88f){
                        overExpose.addAndGet(1);
                    }
                    if(!end){
                        sb.append(" ,");
                    }
                    size ++ ;
                    if(size == 8){
                        sb.append("\r\n");
                        size = 0;
                    }
                    return false;
                }
            });
            sb.append(")\r\n ").append(String.format("overDark/total = %d/%d = ", overDark.get(), list.size()))
                    .append(overDark.get() * 1f/list.size()); //0.5f 左右略微有点暗， 0.6 + 比较暗.0.8+极暗

            sb.append(String.format(",  overExpose/total = %d/%d = ", overExpose.get(), list.size()))
                    .append(overExpose.get() * 1f/list.size());//0.7f曝光比较严重

            FileHelper.writeTo(new File(VEGapUtils.getFileDir(darkPath, 1, true),
                    VEGapUtils.getFileName(darkPath)+ "__hsv_v_info.txt"), sb.toString());
        }
    }

    public static List<List<Float>> getImageBrightness(String image) {
        List<List<Float>> result = new ArrayList<>();
        List<List<Integer>> list = new ArrayList<>();
        getImagePixel(image, list);
        float[] hsv = new float[3];
        for (int size = list.size(), i = 0; i < size; i++) {
            List<Float> tmp = new ArrayList<>();
            List<Integer> cols = list.get(i);
            for (int size2 = cols.size(), i2 = 0; i2 < size2; i2++) {
                Color c = new Color(cols.get(i2));
                Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
                tmp.add(hsv[2]);
            }
            result.add(tmp);
        }
        return result;
    }

    public static void getImagePixel(String image, List<List<Integer>> colors) {
       // int[] rgb = new int[3];
       // float[] hsv = new float[3];
        File file = new File(image);
        BufferedImage bi;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int width = bi.getWidth();
        int height = bi.getHeight();

        int minx = bi.getMinX();
        int miny = bi.getMinY();
        System.out.println("width=" + width + ",height=" + height + ".");
        System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        for (int i = minx; i < width; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字

              /*  rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
                        + rgb[1] + "," + rgb[2] + ")");
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);*/
                cols.add(pixel);
            }
            colors.add(cols);
        }
    }

    private static class Helper {
        final String path;

        public Helper(String path) {
            this.path = path;
        }

        public void start() {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                ColorModel colorModel = image.getColorModel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
