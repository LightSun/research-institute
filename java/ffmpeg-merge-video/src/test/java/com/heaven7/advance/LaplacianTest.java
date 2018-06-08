package com.heaven7.advance;

import com.heaven7.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 拉普拉斯算子测试
 * see {@linkplain Image}
 */
@Deprecated
public class LaplacianTest {

    private final File srcFile;
   // private int[] srcData;
    private int[] grayData;
    private int[] resultData;
    private int w;
    private int h;
    private int imageType;

    public LaplacianTest(String imagePath) {
        srcFile = new File(imagePath);
    }

    public static void main(String[] args) {
        new LaplacianTest("F:\\test\\imgs_tmp\\test.jpg").laplacian();
    }

    public void laplacian(){
        /* 拉普拉斯算子
         {1, 1, 1},
         {1, -8, 1},  和
         {1, 1, 1}

         {0, -1, 0},
         {-1, 4, -1},
         {0, -1, 0}  效果差不多
         */
        double[][] mask = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };
        filter(mask);
        exportFile(resultData, "laplacian_hw_2");
    }

    private void exportFile(int[] data, String suffix) {
        String newFileName = FileUtils.getFileName(srcFile.getAbsolutePath()) + "_" + suffix;
        String fileDir = FileUtils.getFileDir(srcFile.getAbsolutePath(), 1, true);
        String extension = FileUtils.getFileExtension(srcFile);
        File newFile = new File(fileDir, newFileName + "." + extension);
        if(newFile.exists()){
            newFile.delete();
        }

        BufferedImage out = new BufferedImage(w, h , imageType);
        int index = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                 out.setRGB(j, i, data[index++]);
            }
        }
        try {
            ImageIO.write(out, "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toGray(){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(srcFile);
            w = bufferedImage.getWidth();
            h = bufferedImage.getHeight();
            imageType = bufferedImage.getType();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        grayData = new int[w * h];
        int index = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                final int color = bufferedImage.getRGB(j, i);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
               // System.out.println(i + " : " + j + " " + gray);
                int newPixel = colorToRGB(255, gray, gray, gray);
               // grayImage.setRGB(i, j, newPixel);
                grayData[index++] = newPixel; //w w w w ...h个
            }
        }

        exportFile(grayData, "gray");
    }

    public void filter(double[][] mask) {
        toGray();//灰度化
        int mh = mask.length;
        int mw = mask[1].length;
        int sh = (mh + 1) / 2;
        int sw = (mw + 1) / 2;
        double maskSum = sum(mask);
        int[] d = new int[w * h];

        for (int i = (mh - 1) / 2 + 1; i < h - (mh - 1) / 2; i++) {
            for (int j = (mw - 1) / 2 + 1; j < w - (mw - 1) / 2; j++) {
                int s = 0;
                for (int m = 0; m < mh; m++) {
                    for (int n = 0; n < mw; n++) {
                        s = s + (int) (mask[m][n] * this.grayData[j + n - sw + (i + m - sh) * w]);
                    }
                }
                if (maskSum != 0)
                    s /= maskSum;

                if (s < 0)
                    s = 0;
                if (s > 255)
                    s = 255;
               // System.out.println(" d: index = " + (j + i * w));
                d[j + i * w] = s;
            }
        }
        this.resultData = d;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }

    private static double sum(double[][] mask) {
        double val = 0;
        for (double[] row : mask) {
            for (double single : row) {
                val += single;
            }
        }
        return val;
    }
}
