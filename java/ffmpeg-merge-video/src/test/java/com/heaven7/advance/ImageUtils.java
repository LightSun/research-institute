package com.heaven7.advance;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author heaven7
 */
public class ImageUtils {

    /*
     * 图片缩放,w，h为缩放的目标宽度和高度
     * src为源文件目录，dest为缩放后保存目录
     */
    public static void zoomImage(String src, String dest, int w, int h){

        double wr = 0, hr = 0;
        File srcFile = new File(src);
        File destFile = new File(dest);

        BufferedImage bufImg = null; //读取图片
        try {
            bufImg = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板

        wr = w * 1.0 / bufImg.getWidth();     //获取缩放比例
        hr = h * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Image scaledImage = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) scaledImage, dest.substring(dest.lastIndexOf(".") + 1), destFile); //写入缩减后的图片
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * 图片按比率缩放
     * size为文件大小
     */
    public static void zoomImage2(String src, String dest, int maxWidth, int maxHeight, RenderingHints hints){
        File srcFile = new File(src);
        File destFile = new File(dest);

        BufferedImage bufImg = null;
        double ratio = 1f;
        try {
            bufImg = ImageIO.read(srcFile);
            int width = bufImg.getWidth();
            int height = bufImg.getHeight();
            double ratioW = 1f, ratioH = 1f;
            if(width > maxWidth){
                ratioW =  maxWidth * 1d / width;
            }
            if(height > maxHeight){
                ratioH =  maxHeight * 1d / height;
            }
            ratio = Math.min(ratioW, ratioH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Image newImage =  bufImg.getScaledInstance(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.SCALE_SMOOTH);

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), hints);
        BufferedImage Itemp = ato.filter(bufImg, null);
        try {
            ImageIO.write(Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String src = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\绣花小西装\\1-8.jpg";
        String dst = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\绣花小西装\\1-8_scaled.jpg";
        String dst2 = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\绣花小西装\\1-8_scaled2.jpg";
        zoomImage(src, dst, 400, 400);
        zoomImage2(src, dst2, 400, 400, null);

        String dst3 = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\绣花小西装\\1-8_scaled3.jpg";
        String dst4 = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\绣花小西装\\1-8_scaled4.jpg";
        RenderingHints hints1 = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        RenderingHints hints2 = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        zoomImage2(src, dst3, 400, 400, hints1);
        zoomImage2(src, dst4, 400, 400, hints2);
    }
}
