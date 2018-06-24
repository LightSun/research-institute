package com.heaven7.advance;


/**
 * 图像模糊相关处理
 *
 * @author heaven7
 */
public class ImageBlur {

    private final int width;
    private final int height;
    private Integer[] data;

    public ImageBlur(Matrix2<Integer> imageMat) {
        data = imageMat.toArray();
        width = imageMat.getRowCount();
        height = imageMat.getColumnCount();
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int[] laplacian(){
        return laplacian(true, true);
    }
    public int[] laplacian(boolean preGray, boolean postGray){
        if(preGray){
            toGray();
        }
        double[][] mask = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };
        int[] arr = filter(mask);
        if(postGray){
            final int h = height;
            final int w = width;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int old = arr[j + i * w];
                    arr[j + i * w] = (255 << 24) | (old << 16) | (old << 8) | (old);
                }
            }
        }
        return arr;
    }

    public int[] filter(double[][] mask) {
        int mh = mask.length;
        int mw = mask[1].length;
        int sh = (mh + 1) / 2;
        int sw = (mw + 1) / 2;
        double maskSum = sum(mask);
        int[] d = new int[width * height];

        for (int i = (mh - 1) / 2 + 1; i < height - (mh - 1) / 2; i++) {
            for (int j = (mw - 1) / 2 + 1; j < width - (mw - 1) / 2; j++) {
                int s = 0;
                for (int m = 0; m < mh; m++) {
                    for (int n = 0; n < mw; n++) {
                        s = s + (int) (mask[m][n] * this.data[j + n - sw + (i + m - sh) * width]);
                    }
                }
                if (maskSum != 0)
                    s /= maskSum;

                if (s < 0)
                    s = 0;
                if (s > 255)
                    s = 255;
                d[j + i * width] = s;
            }
        }
        return d;
    }
    public void toGray() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.data[x + y * width] = grayColor(data[x + y * width]); //to gray
            }
        }
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
