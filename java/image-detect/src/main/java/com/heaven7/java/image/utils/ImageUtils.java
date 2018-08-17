package com.heaven7.java.image.utils;

import com.heaven7.java.image.Matrix2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class ImageUtils {

    public static Matrix2<Integer> image2Matrix(BufferedImage image){
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
}
