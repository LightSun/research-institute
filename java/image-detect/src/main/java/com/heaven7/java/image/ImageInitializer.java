package com.heaven7.java.image;

import com.heaven7.java.image.detect.AbstractVideoManager;
import com.heaven7.java.image.detect.ImageDetector;

/** @author heaven7 */
public class ImageInitializer {

    private ImageReader imageReader;
    private ImageWriter imageWriter;
    private ImageDetector imageDetector;
    private Matrix2Transformer matrix2Transformer;
    private AbstractVideoManager.VideoFrameDelegate videoFrameDelegate;

    protected ImageInitializer(ImageInitializer.Builder builder) {
        this.imageReader = builder.imageReader;
        this.imageWriter = builder.imageWriter;
        this.imageDetector = builder.imageDetector;
        this.matrix2Transformer = builder.matrix2Transformer;
        this.videoFrameDelegate = builder.videoFrameDelegate;
    }

    public void setImageReader(ImageReader imageReader) {
        this.imageReader = imageReader;
    }
    public void setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
    }

    public void setImageDetector(ImageDetector imageDetector) {
        this.imageDetector = imageDetector;
    }
    public void setMatrix2Transformer(Matrix2Transformer matrix2Transformer) {
        this.matrix2Transformer = matrix2Transformer;
    }
    public void setVideoFrameDelegate(AbstractVideoManager.VideoFrameDelegate videoFrameDelegate) {
        this.videoFrameDelegate = videoFrameDelegate;
    }

    public ImageReader getImageReader() {
        return this.imageReader;
    }

    public ImageWriter getImageWriter() {
        return this.imageWriter;
    }

    public ImageDetector getImageDetector() {
        return this.imageDetector;
    }

    public Matrix2Transformer getMatrix2Transformer() {
        return this.matrix2Transformer;
    }

    public AbstractVideoManager.VideoFrameDelegate getVideoFrameDelegate() {
        return this.videoFrameDelegate;
    }

    public static class Builder {
        private ImageReader imageReader;
        private ImageWriter imageWriter;
        private ImageDetector imageDetector;
        private Matrix2Transformer matrix2Transformer;
        private AbstractVideoManager.VideoFrameDelegate videoFrameDelegate;

        public Builder setImageReader(ImageReader imageReader) {
            this.imageReader = imageReader;
            return this;
        }

        public Builder setImageWriter(ImageWriter imageWriter) {
            this.imageWriter = imageWriter;
            return this;
        }

        public Builder setImageDetector(ImageDetector imageDetector) {
            this.imageDetector = imageDetector;
            return this;
        }

        public Builder setMatrix2Transformer(Matrix2Transformer matrix2Transformer) {
            this.matrix2Transformer = matrix2Transformer;
            return this;
        }

        public Builder setVideoFrameDelegate(AbstractVideoManager.VideoFrameDelegate videoFrameDelegate) {
            this.videoFrameDelegate = videoFrameDelegate;
            return this;
        }

        public ImageInitializer build() {
            return new ImageInitializer(this);
        }
    }
}
