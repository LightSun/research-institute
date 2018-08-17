package com.heaven7.test;

import com.heaven7.java.image.*;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author heaven7
 */
public class MergeImageTest {

    public static final ImageReader IMAGE_READER = new JavaImageReader();
    public static final ImageWriter IMAGE_WRITER = new JavaImageWriter();
    public static final Matrix2Transformer IMAGE_TRANSFORMER = new JavaMatrix2Transformer();

    @Test
    public void testWrite(){
        String file = "E:\\tmp\\upload_files\\img_00001.jpg";
        String dst = "E:\\tmp\\upload_files\\img_00001_copy.jpg";
        Matrix2<Integer> mat1 = IMAGE_READER.readMatrix(file).getMat();
        IMAGE_WRITER.write(mat1, new File(dst), BufferedImage.TYPE_INT_RGB, "jpg");
    }

    @Test
    public void testMergeColumn(){
        String[] imgs = {
                "E:\\tmp\\upload_files\\img_00001.jpg",
                "E:\\tmp\\upload_files\\img_00012.jpg",
        };
        Matrix2<Integer> mat1 = IMAGE_READER.readMatrix(imgs[0]).getMat();
        Matrix2<Integer> mat2 = IMAGE_READER.readMatrix(imgs[1]).getMat();

        Matrix2Utils.mergeByColumn(mat1, mat2);//竖直拼接

        write(mat1, "E:\\tmp\\upload_files\\img_0000_merge12.jpg");
    }

    @Test
    public void testMergeRow(){
        String[] imgs = {
                "E:\\tmp\\upload_files\\img_00001.jpg",
                "E:\\tmp\\upload_files\\img_00012.jpg",
        };
        Matrix2<Integer> mat1 = IMAGE_READER.readMatrix(imgs[0]).getMat();
        Matrix2<Integer> mat2 = IMAGE_READER.readMatrix(imgs[1]).getMat();

        Matrix2Utils.mergeByRow(mat1, mat2);// 水平拼接

        write(mat1, "E:\\tmp\\upload_files\\img_0000_merge12_row.jpg");
    }

    @Test
    public void testBytes(){
        String file = "E:\\tmp\\upload_files\\img_0000_merge12.jpg";
        String dst = "E:\\tmp\\upload_files\\img_0000_merge12_from_bytes.jpg";
        Matrix2<Integer> mat1 = IMAGE_READER.readMatrix(file).getMat();
        ByteArrayInputStream in = new ByteArrayInputStream(IMAGE_TRANSFORMER.transform(mat1, BufferedImage.TYPE_INT_RGB, "jpg"));
        try {
            BufferedImage image = ImageIO.read(in);
            ImageIO.write(image, "jpg",  new File(dst));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Matrix2<Integer> mat,String dst){
        IMAGE_WRITER.write(mat, new File(dst), BufferedImage.TYPE_INT_RGB, "jpg");
    }
    /**

找不到文件“MakePri.exe”e

     */
}
