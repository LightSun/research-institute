package com.heaven7.ve.starter;

import com.heaven7.java.image.ImageWriter;
import com.heaven7.java.image.Matrix2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author heaven7
 */
public class JavaImageWriter implements ImageWriter {

    public static final JavaImageWriter DEFAULT = new JavaImageWriter();

    @Override
    public boolean write(Matrix2<Integer> mat, File dst, int imageType, String format) {
        int w = mat.getRowCount();
        int h = mat.getColumnCount();
        // System.out.println(w);
        // System.out.println(h);
        BufferedImage image = new BufferedImage(w, h, imageType);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                image.setRGB(i, j, mat.getRawValues().get(i).get(j));
            }
        }
        try {
            ImageIO.write(image, format, dst);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean write(byte[] imageData, File dst, String format) {
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ImageIO.write(image, format, dst);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
