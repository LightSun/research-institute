package com.heaven7.test;

import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.Matrix2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaImageReader implements ImageReader {

    @Override
    public ImageInfo read(String img) {
        File srcFile = new File(img);
        BufferedImage image;
        int imageType;
        try {
            image = ImageIO.read(srcFile);
            imageType = image.getType();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int w = image.getWidth();
        int h = image.getHeight();
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < w; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < h; j++) {
                cols.add(image.getRGB(i, j));
            }
            list.add(cols);
        }
        return new ImageInfo(new Matrix2<>(list), imageType);
    }

    @Override
    public byte[] readBytes(String imgFile,String format) {
        File srcFile = new File(imgFile);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return baos.toByteArray();
    }
}
