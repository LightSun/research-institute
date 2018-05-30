package com.heaven7.advance;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

/**
 * 图像曝光度. https://blog.csdn.net/qq249356520/article/details/78864244
 */
public class ImageExposeTest {

    public static void getImagePixel(String image) throws Exception {
        int[] rgb = new int[3];
        float[] hsv = new float[3];
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
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字

                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
                        + rgb[1] + "," + rgb[2] + ")");
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);
            }
        }
    }

    private static class Helper{
        final String path;

        public Helper(String path) {
            this.path = path;
        }

        public void start(){
            try {
                BufferedImage image = ImageIO.read(new File(path));
                ColorModel colorModel = image.getColorModel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
