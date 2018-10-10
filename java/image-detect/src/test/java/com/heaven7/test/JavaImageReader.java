package com.heaven7.test;

import com.heaven7.java.image.ImageLimitInfo;
import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaImageReader implements ImageReader {

    @Override
    public ImageInfo readMatrix(String img, ImageLimitInfo info) {
        File srcFile = new File(img);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException("srcFile = " + img, e);
        }
        return ImageUtils.readMatrix(image, info);
    }

    @Override
    public ImageInfo readBytes(String imgFile,String format, ImageLimitInfo info) {
        File srcFile = new File(imgFile);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ImageUtils.readBytes(image, format, info);
    }

    @Override
    public ImageInfo readMatrix(InputStream in, ImageLimitInfo info) {
        BufferedImage image;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return ImageUtils.readMatrix(image, info);
    }

    @Override
    public int[] readWidthHeight(InputStream in) {
        BufferedImage image;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new int[]{image.getWidth(), image.getHeight()};
    }

    @Override
    public int[] readWidthHeight(String imageFile) {
       InputStream in = null;
        try {
            in = new FileInputStream(imageFile);
            return readWidthHeight(in);
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            com.vida.common.IOUtils.closeQuietly(in);
        }
    }

}
