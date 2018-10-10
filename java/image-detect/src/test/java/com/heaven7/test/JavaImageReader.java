package com.heaven7.test;

import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.heaven7.java.image.utils.ImageUtils.image2Matrix;

public class JavaImageReader implements ImageReader {

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
        imageType = ImageFactory.getImageInitializer().getImageTypeTransformer().nativeToPublic(imageType);
        ImageInfo imageInfo = new ImageInfo(image2Matrix(image), imageType);
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
    }

    @Override
    public ImageInfo readBytes(String imgFile,String format) {
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
        int imageType = ImageFactory.getImageInitializer().getImageTypeTransformer().nativeToPublic(image.getType());
        ImageInfo imageInfo = new ImageInfo(baos.toByteArray(), imageType);
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
        int imageType = ImageFactory.getImageInitializer().getImageTypeTransformer().nativeToPublic(image.getType());
        ImageInfo imageInfo = new ImageInfo(image2Matrix(image), imageType);
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
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
