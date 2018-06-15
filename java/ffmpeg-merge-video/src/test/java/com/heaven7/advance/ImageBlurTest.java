package com.heaven7.advance;

import com.heaven7.utils.FileUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图像模糊相关处理， 思路
 * 1. 灰度化-》 拉普拉斯卷积算法 -》方差
 * https://blog.csdn.net/sinat_24648637/article/details/49661669
 *
 * @author heaven7
 */
public class ImageBlurTest {

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
        System.out.println(mat.getWidth());
        System.out.println(mat.getHeight());
        //now mat2 is same with mat
        Matrix2<Integer> mat2 = Matrix2.ofObjectArray(w, h, mat.toArray()).transpose();
        //now mat3 is same with mat
        Matrix2<Integer> mat3 = Matrix2.ofObjectArray(h, w, mat.transpose().toArray());

        testGenImage(srcFile, mat, image.getType(), "test");
        testGenImage(srcFile, mat.turnRight90(), image.getType(), "test_turnRight90");
        testGenImage(srcFile, mat.turnLeftRight(), image.getType(), "test_turnLeftRight");
        testGenImage(srcFile, mat.turnTopBottom(), image.getType(), "test_turnTopBottom");
        testGenImage(srcFile, mat2, image.getType(), "test3");
        testGenImage(srcFile, mat3, image.getType(), "test4");

        testGenImage(srcFile, mat.rotateClockwise(90), image.getType(), "test_rotateClockwise_90");
        testGenImage(srcFile, mat.rotateClockwise(180), image.getType(), "test_rotateClockwise_180");
        testGenImage(srcFile, mat.rotateClockwise(270), image.getType(), "test_rotateClockwise_270");

        // System.out.println("variance: " + (Matrix2Utils.varIntFloat(mat) > 100000)); // true
    }

    @Test
    public void testCannyEdgeFilter(){
       // String path = "F:\\test\\imgs_tmp\\test.jpg";
        String path = "F:\\test\\imgs\\story0\\churchIn\\C0181\\img_00012.png";
        File srcFile = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CannyEdgeFilter filter = new CannyEdgeFilter();

        BufferedImage result = filter.filter(image, null);
        File dstFile = getTargetFile(srcFile, "cannyEdgeFilter");
        try {
            ImageIO.write(result, "png", dstFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPng(){
        String path = "F:\\test\\imgs\\story0\\churchIn\\C0181\\img_00008.png";
        File srcFile = new File(path);
        JavaImageReader reader = new JavaImageReader();
        Matrix2<Integer> mat = reader.read(path);
        int imageType = reader.getImageType();
        int w = mat.getWidth();
        int h = mat.getHeight();

        ImageBlur blur = new ImageBlur(mat);
        int[] data = blur.laplacian(true, false);

        Matrix2<Integer> mat4 = Matrix2.ofIntArray(blur.getWidth(), blur.getHeight(), data).transpose();
        testGenImage(srcFile, mat4, imageType, "laplacian");
        System.out.println("variance: " + (Matrix2Utils.varIntFloat(mat4)));
    }

    @Test //jpg
    public void test1() {
        String path = "F:\\test\\imgs_tmp\\test.jpg";
        File srcFile = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(srcFile);
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
        Matrix2<Integer> mat = new Matrix2<>(list);
        ImageBlur blur = new ImageBlur(mat);
        int[] data = blur.laplacian(true, false);
        //image type 增加透明通道
        testGenImage(srcFile,
                toImage(blur.getWidth(), blur.getHeight(), BufferedImage.TYPE_INT_ARGB, data),
                "laplacian_new");
        //原始图片类型
        Matrix2<Integer> mat2 = Matrix2.ofIntArray(blur.getWidth(), blur.getHeight(), data);
        testGenImage(srcFile, mat2, image.getType(), "laplacian_new_2");
        //原始图片类型 ， 结果： 相当于右旋转90
        testGenImage(srcFile,
                toImage(blur.getWidth(), blur.getHeight(), image.getType(), data),
                "laplacian_new_3");
        //原始图片类型
        Matrix2<Integer> mat4 = Matrix2.ofIntArray(blur.getWidth(), blur.getHeight(), data).transpose();
        testGenImage(srcFile, mat4, image.getType(), "laplacian_new_4");
        System.out.println("variance: " + (Matrix2Utils.varIntFloat(mat4)));
    }

    private static BufferedImage toImage(int w, int h, int imageType, int[] data) {
        BufferedImage image = new BufferedImage(w, h, imageType);
        image.setRGB(0, 0, w, h, data, 0, w);
        return image;
    }

    private static void testGenImage(File srcFile, BufferedImage img, String suffix) {
        File newFile = getTargetFile(srcFile, suffix);
        try {
            ImageIO.write(img, "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testGenImage(File srcFile, Matrix2<Integer> mat, int imageType, String suffix) {
        int w = mat.getWidth();
        int h = mat.getHeight();
        System.out.println(w);
        System.out.println(h);
        BufferedImage image = new BufferedImage(w, h, imageType);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
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
        if (newFile.exists()) {
            newFile.delete();
        }
        return newFile;
    }

    static class JavaImageReader implements ImageReader{

        private int imageType;

        @Override
        public Matrix2<Integer> read(String img) {
            File srcFile = new File(img);
            BufferedImage image;
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
            return new Matrix2<>(list);
        }
        public int getImageType() {
            return imageType;
        }
    }
}
