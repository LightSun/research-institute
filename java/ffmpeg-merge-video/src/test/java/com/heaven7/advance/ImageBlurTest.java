package com.heaven7.advance;

import com.heaven7.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图像模糊相关处理
 *
 * @author heaven7
 */
public class ImageBlurTest {

    private static void testImage(File srcFile, Matrix2<Integer> mat, int imageType, String suffix){
        int w = mat.getWidth();
        int h = mat.getHeight();
        System.out.println(w);
        System.out.println(h);
        BufferedImage image = new BufferedImage(w, h, imageType);
        for (int i = 0 ; i < w ; i ++){
            for (int j = 0 ; j < h ; j ++){
                image.setRGB(i, j, mat.getRawValues().get(i).get(j));
            }
        }
        File newFile = getTargetFile(srcFile, suffix);
        try {
            ImageIO.write(image, "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static File getTargetFile(File srcFile, String suffix) {
        String newFileName = FileUtils.getFileName(srcFile.getAbsolutePath()) + "_" + suffix;
        String fileDir = FileUtils.getFileDir(srcFile.getAbsolutePath(), 1, true);
        String extension = FileUtils.getFileExtension(srcFile);
        File newFile = new File(fileDir, newFileName + "." + extension);
        if(newFile.exists()){
            newFile.delete();
        }
        return newFile;
    }

    public static void main(String[] args) throws IOException {
        String path = "F:\\test\\imgs_tmp\\test.jpg";
        File srcFile = new File(path);
        BufferedImage image = ImageIO.read(srcFile);
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
        Matrix2<Integer> mat = new Matrix2<>(list);
        Integer[] array = mat.toArray(null);
        Matrix2<Integer> mat2 = Matrix2.ofArray(w, h, array).transpose();

        testImage(srcFile, mat, image.getType(), "test");
        testImage(srcFile, mat.turnLeftRight(), image.getType(), "test_turnLeftRight");
        testImage(srcFile, mat.turnTopBottom(), image.getType(), "test_turnTopBottom");
        testImage(srcFile, mat2, image.getType(), "test3");
    }
}
