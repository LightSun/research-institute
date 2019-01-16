package com.heaven7.java.image.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * @param <I> the image.
 * @author heaven7
 */
public interface ImageScaleDelegate<I>{

    /**
     * used for equal scale. scale then clip
     */
    byte MODE_SCALE_CLIP = 1;
    /**
     * used for equal scale. clip then scale
     */
    byte MODE_CLIP_SCALE = 2;
    /**
     * keep the all image, only scale. not the may make image stretch.
     */
    byte MODE_RAW_SCALE  = 3;


    /**
     * do scale the image .
     * @param context the context info
     * @param input the input image
     * @param expectW the expect width
     * @param expectH the expect height
     * @param centerX the focus center x in image used for clip . for mode {@linkplain #MODE_RAW_SCALE} this is ignore.
     * @param centerY the focus center y in image used for clip . for mode {@linkplain #MODE_RAW_SCALE} this is ignore.
     * @param mode the mode .see {@linkplain ImageScaleDelegate#MODE_CLIP_SCALE} and etc.
     * @return the scaled image.
     */
    I scale(Object context, I input, int expectW, int expectH, int centerX, int centerY, byte mode);

    class DefaultImageScaleDelegate implements ImageScaleDelegate<BufferedImage>{
        @Override
        public BufferedImage scale(Object context, BufferedImage input, int expectW,
                            int expectH,  int centerX, int centerY, byte mode) {
            float wRate = expectW * 1f / input.getWidth() ;
            float hRate = expectH * 1f / input.getHeight();
            switch (mode){
                case MODE_RAW_SCALE: {
                    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wRate, hRate), null);
                    return ato.filter(input, null);
                }

                case MODE_CLIP_SCALE: {
                    float rate;
                    int keepW , keepH;
                    if(expectW > input.getWidth() || expectH > input.getHeight()){
                        //scale up.
                        //first max
                        if(wRate > hRate){
                             keepW = input.getWidth();
                             keepH = keepW * expectH / expectW;
                        }else {
                             keepH = input.getHeight();
                             keepW = keepH * expectW / expectH;
                        }
                    }else {
                        //first min
                        if(input.getWidth() > input.getHeight()){
                            keepH = input.getHeight();
                            keepW = keepH * expectW / expectH;
                        }else{
                            keepW = input.getWidth();
                            keepH = keepW * expectH / expectW;
                        }
                    }
                    //if image is 800 * 600. center x = 200, center y = 300.
                    int startX = centerX - keepW / 2;
                    int endX = startX + keepW ;
                    int startY = centerY - keepH / 2;
                    int endY = startY + keepH ;
                    BufferedImage clipped = ImageUtils.crop(input, startX, endX, startY, endY);
                    //rate is changed
                    rate = expectW * 1f/ clipped.getWidth();
                    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
                    return ato.filter(clipped, null);
                }

                case MODE_SCALE_CLIP:{
                    float rate;
                    if(expectW > input.getWidth() || expectH > input.getHeight()) {
                        //scale up.
                        //scale
                        rate = Math.max(wRate, hRate);
                    }else{
                        rate = Math.min(wRate, hRate);
                    }
                    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
                    input = ato.filter(input, null);
                    int scalledCx = (int) (centerX * rate);
                    int scalledCy = (int) (centerY * rate);

                    int keepW = (int) (rate * input.getWidth());
                    int keepH = (int) (rate * input.getHeight());
                    int startX = scalledCx - keepW / 2;
                    int endX = startX + keepW;
                    int startY = scalledCy - keepH / 2;
                    int endY = startY + keepH;
                    return ImageUtils.crop(input, startX, endX, startY, endY);
                }

                default:
                    throw new UnsupportedOperationException("mode = " + mode);
            }
        }
    }

}
