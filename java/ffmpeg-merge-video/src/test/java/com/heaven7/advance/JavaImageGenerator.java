package com.heaven7.advance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JavaImageGenerator implements ImageGenerator {

    @Override
    public boolean generate(Matrix2<Integer> mat, File dst, int imageType, String format) {
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
}
