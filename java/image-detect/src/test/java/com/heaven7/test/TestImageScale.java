package com.heaven7.test;

import com.heaven7.java.image.utils.ImageScaleDelegate;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author heaven7
 */
public class TestImageScale {

    @Test
    public void testClipScale_down()throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 500, 500,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }

    @Test
    public void testClipScale_down2()throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale2.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 300, 800,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }
    @Test
    public void testClipScale_down3()throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale3.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 800, 300,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }

    @Test
    public void testClipScale_up() throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale_up.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 250, 1000,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }

    @Test
    public void testClipScale_up2() throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale_up2.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 1000, 250,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }
    @Test // have bugs
    public void testClipScale_up3() throws Exception{
        //1000 * 1498
        String imageFile = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale.jpg";
        String out_clip_scale = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\白色吊带长裙\\DSC_2329_clip_scale_up3.jpg";
        BufferedImage input = ImageIO.read(new File(imageFile));
        ImageScaleDelegate.DefaultImageScaleDelegate delegate = new ImageScaleDelegate.DefaultImageScaleDelegate();
        BufferedImage img_clip_scale = delegate.scale(null, input, 1500, 800,
                input.getWidth() / 2, input.getHeight() / 2, ImageScaleDelegate.MODE_CLIP_SCALE);
        ImageIO.write(img_clip_scale, "jpg", new File(out_clip_scale));
    }
}
