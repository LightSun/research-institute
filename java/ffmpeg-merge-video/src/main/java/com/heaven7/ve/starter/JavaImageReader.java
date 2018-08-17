package com.heaven7.ve.starter;

import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.Matrix2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.heaven7.java.image.utils.ImageUtils.image2Matrix;

public class JavaImageReader implements ImageReader {

    public static final JavaImageReader DEFAULT = new JavaImageReader();

    @Override
    public ImageInfo readMatrix(String img) {
        File srcFile = new File(img);
        BufferedImage image;
        int imageType;
        try {
            image = ImageIO.read(srcFile);
            imageType = image.getType();
        } catch (IOException e) {
            throw new RuntimeException("srcFile = " + img, e);
        }
        ImageInfo imageInfo = new ImageInfo(image2Matrix(image), imageType);
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
    }

    @Override
    public ImageInfo readBytes(String imgFile, String format) {
        File srcFile = new File(imgFile);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException("file = " + imgFile, e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ImageInfo imageInfo = new ImageInfo(baos.toByteArray(), image.getType());
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
    }

    @Override
    public ImageInfo readMatrix(InputStream in) {
        BufferedImage image;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageInfo imageInfo = new ImageInfo(image2Matrix(image), image.getType());
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
    }

}
