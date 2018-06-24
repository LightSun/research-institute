package com.heaven7.advance;

import com.heaven7.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 拉普拉斯算子
 */
public class Image {

    private final File srcFile;
    private final int imageType;
    public int h; //高
    public int w; //宽
    public int[] data; //像素
    public boolean gray = true; //是否为灰度图像

    public Image(String imagePath){
        this.srcFile = new File(imagePath);

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(srcFile);
            w = bufferedImage.getWidth();
            h = bufferedImage.getHeight();
            imageType = bufferedImage.getType();
            this.data = bufferedImage.getRGB(0, 0, w, h, null, 0, w);
            //just test
           /* Matrix2<Integer> mat = toMatrix().transpose();
            System.out.println(mat.getRowCount());
            System.out.println(mat.getColumnCount());
            BufferedImage image = new BufferedImage(this.w, this.h, imageType);
            for (int i = 0 ; i < w ; i ++){
                for (int j = 0 ; j < h ; j ++){
                    image.setRGB(i, j, mat.getRawValues().get(i).get(j));
                }
            }
            File newFile = getTargetFile("revert");
            ImageIO.write(image, "jpg", newFile);*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Image("F:\\test\\imgs_tmp\\test.jpg").laplacian();
       /* String dir = "F:\\test\\img_blur";
        VisitServices.from(FileUtils.getFiles(new File(dir), "jpg"))
                .fire(new FireVisitor<String>() {
                    @Override
                    public Boolean visit(String path, Object param) {
                        new Image(path).laplacian();
                        return null;
                    }
                });*/
    }

    public void laplacian(){
        double[][] mask = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };
        filter(mask);

        File newFile = getTargetFile("laplacian");
        try {
            ImageIO.write(toImage(), "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getTargetFile(String suffix) {
        String newFileName = FileUtils.getFileName(srcFile.getAbsolutePath()) + "_" + suffix;
        String fileDir = FileUtils.getFileDir(srcFile.getAbsolutePath(), 1, true);
        String extension = FileUtils.getFileExtension(srcFile);
        File newFile = new File(fileDir, newFileName + "." + extension);
        if(newFile.exists()){
            newFile.delete();
        }
        return newFile;
    }

    public Matrix2<Integer> toMatrix(){
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < w; j++) {
                cols.add(data[i * w + j]);
            }
            list.add(cols);
        }
        return new Matrix2<>(list);
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_ARGB );
        int[] d = new int[w * h];
        for (int i = 0; i < this.h; i++) {
            for (int j = 0; j < this.w; j++) {
                if (this.gray) {
                    d[j + i * this.w] = (255 << 24) | (data[j + i * this.w] << 16) | (data[j + i * this.w] << 8) | (data[j + i * this.w]);
                   // d[j + i * this.w] = grayColor(data[j + i * this.w]);
                } else {
                    d[j + i * this.w] = data[j + i * this.w];
                }
            }
        }
        image.setRGB(0, 0, w, h, d, 0, w);
        return image;
    }

    public void toGray() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = this.data[x + y * w];
                int R = (c >> 16) & 0xFF;
                int G = (c >> 8) & 0xFF;
                int B = (c >> 0) & 0xFF;
                this.data[x + y * w] = (int) (0.3f * R + 0.59f * G + 0.11f * B); //to gray
                //h代表行数， w代表列数
               // System.out.println(String.format("第 %d 行， %d 列, color = %d", y, x, data[x + y * w]));
            }
        }
    }
    public void filter(double[][] mask) {
        toGray();//灰度化
        int mh = mask.length;
        int mw = mask[1].length;
        int sh = (mh + 1) / 2;
        int sw = (mw + 1) / 2;
        double maskSum = sum(mask);
        int[] d = new int[w * h];

        for (int i = (mh - 1) / 2 + 1; i < h - (mh - 1) / 2; i++) {
            for (int j = (mw - 1) / 2 + 1; j < w - (mw - 1) / 2; j++) {
                int s = 0;
                for (int m = 0; m < mh; m++) {
                    for (int n = 0; n < mw; n++) {
                        s = s + (int) (mask[m][n] * this.data[j + n - sw + (i + m - sh) * w]);
                    }
                }
                if (maskSum != 0)
                    s /= maskSum;

                if (s < 0)
                    s = 0;
                if (s > 255)
                    s = 255;
                d[j + i * w] = s;
            }
        }
        this.data = d;
    }
    private static int grayColor(int c){
        int R = (c >> 16) & 0xFF;
        int G = (c >> 8) & 0xFF;
        int B = c & 0xFF;
        return (int) (0.3f * R + 0.59f * G + 0.11f * B); //to gray
    }

    private static double sum(double[][] mask) {
        double val = 0;
        for (double[] row : mask) {
            for (double single : row) {
                val += single;
            }
        }
        return val;
    }

}
