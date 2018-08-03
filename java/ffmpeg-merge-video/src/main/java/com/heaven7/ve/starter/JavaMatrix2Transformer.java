package com.heaven7.ve.starter;

import com.heaven7.java.image.Matrix2;
import com.heaven7.java.image.Matrix2Transformer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author heaven7
 */
public class JavaMatrix2Transformer implements Matrix2Transformer {

    @Override
    public byte[] transform(Matrix2<Integer> mat, int imageType, String format) {
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
