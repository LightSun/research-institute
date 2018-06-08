package com.heaven7.advance;

import com.heaven7.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 几种灰度化的方法
 分量法：使用RGB三个分量中的一个作为灰度图的灰度值。
 最值法：使用RGB三个分量中最大值或最小值作为灰度图的灰度值。
 均值法：使用RGB三个分量的平均值作为灰度图的灰度值。

 加权法：由于人眼颜色敏感度不同，按下一定的权值对RGB三分量进行加权平均能得到较合理的灰度图像。
 一般情况按照：Y = 0.30R + 0.59G + 0.11B。

 [注]加权法实际上是取一幅图片的亮度值作为灰度值来计算，用到了YUV模型。在[3]中会发现作者使用了Y = 0.21 * r + 0.71 * g + 0.07 * b来计算灰度值
 （显然三个权值相加并不等于1，可能是作者的错误？）。
 实际上，这种差别应该与是否使用伽马校正有关[1]。
 */
public class ImageGrayTest {

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
    public static void main(String[] args) throws IOException {
        File file = new File("F:\\test\\imgs_tmp\\test.jpg");
        BufferedImage bufferedImage = ImageIO.read(file);
        BufferedImage grayImage =
                new BufferedImage(bufferedImage.getWidth(),
                        bufferedImage.getHeight(),
                        bufferedImage.getType());

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                final int color = bufferedImage.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                System.out.println(i + " : " + j + " " + gray);
                int newPixel = colorToRGB(255, gray, gray, gray);
                grayImage.setRGB(i, j, newPixel);
            }
        }

        String newFileName = FileUtils.getFileName(file.getAbsolutePath()) + "_gray";
        String fileDir = FileUtils.getFileDir(file.getAbsolutePath(), 1, true);
        String extension = FileUtils.getFileExtension(file);
        File newFile = new File(fileDir, newFileName + "." + extension);
        ImageIO.write(grayImage, "jpg", newFile);
    }
}
