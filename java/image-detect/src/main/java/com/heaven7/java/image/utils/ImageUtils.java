package com.heaven7.java.image.utils;

import com.heaven7.java.base.anno.Platform;
import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageLimitInfo;
import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.Matrix2;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class ImageUtils {

    @Platform
    public static Matrix2<Integer> image2Matrix(BufferedImage image) {
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
        return new Matrix2<>(list);
    }

    @Platform //exclude the mat
    public static ImageReader.ImageInfo createBaseImageInfo(BufferedImage image) {
        int imageType = ImageFactory.getImageInitializer().getImageTypeTransformer().nativeToPublic(image.getType());
        ImageReader.ImageInfo imageInfo = new ImageReader.ImageInfo();
        imageInfo.setImageType(imageType);
        imageInfo.setWidth(image.getWidth());
        imageInfo.setHeight(image.getHeight());
        return imageInfo;
    }

    @Platform
    public static ImageReader.ImageInfo readMatrix(BufferedImage image, ImageLimitInfo info) {
        ImageReader.ImageInfo imageInfo = createBaseImageInfo(image);
        //scale if need
        if (info != null && info.hasLimitWidthOrHeight()) {
            float scaleRate = computeScaleRate(image.getWidth(), image.getHeight(), info.getMaxWidth(), info.getMaxHeight());
            if (scaleRate != 1f) {
                AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(scaleRate, scaleRate), null);
                image = ato.filter(image, null);
            }
            imageInfo.setWidthRate(scaleRate);
            imageInfo.setHeightRate(scaleRate);
        }
        imageInfo.setMat(image2Matrix(image));
        return imageInfo;
    }

    @Platform
    public static ImageReader.ImageInfo readBytes(BufferedImage image, String format, ImageLimitInfo info) {
        ImageReader.ImageInfo imageInfo = createBaseImageInfo(image);
        //scale if need
        if (info != null && info.hasLimitWidthOrHeight()) {
            float scaleRate = computeScaleRate(image.getWidth(), image.getHeight(), info.getMaxWidth(), info.getMaxHeight());
            if (scaleRate != 1f) {
                AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(scaleRate, scaleRate), null);
                image = ato.filter(image, null);
            }
            imageInfo.setWidthRate(scaleRate);
            imageInfo.setHeightRate(scaleRate);
        }
        //to byte array.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        imageInfo.setData(baos.toByteArray());
        return imageInfo;
    }

    public static float computeScaleRate(int width, int height, int maxWidth, int maxHeight) {
        float ratioW = 1f, ratioH = 1f;
        if (width > maxWidth) {
            ratioW = maxWidth * 1f / width;
        }
        if (height > maxHeight) {
            ratioH = maxHeight * 1f / height;
        }
        return Math.min(ratioW, ratioH);
    }

    /**
     * @param source the src image
     * @param startX the start x, include
     * @param startY the start y, include
     * @param endX the end x, exclude
     * @param endY the end y, exclude
     * @return cropped image
     */
    public static BufferedImage crop(BufferedImage source, int startX, int endX, int startY, int endY) {
        if (startX < 0) {
            endX -= startX;
            startX = 0;
        }else if(endX > source.getWidth()){
            int diff = endX - (source.getWidth());
            endX = source.getWidth();
            startX -= diff;
        }
        if (startY < 0) {
            endY -= startY;
            startY = 0;
        }else if(endY > source.getHeight()){
            int diff = endY - source.getHeight();
            endY = source.getHeight();
            startY -= diff;
        }
        if(startX < 0 || startY < 0 || endX > source.getWidth() || endY > source.getHeight()){
            throw new IllegalStateException();
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, source.getType());
       // System.out.println("result w = " + result.getWidth() + " ,h = " + result.getHeight());
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX ; x++) {
                int rgb = source.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String file = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329.jpg";
        String outFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_test.jpg";
        try {
            BufferedImage in = ImageIO.read(new File(file));
            BufferedImage out = crop(in, 300, 800, 500, 1000);
            ImageIO.write(out, "jpg", new File(outFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
