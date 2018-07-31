package com.heaven7.java.image;

import com.heaven7.java.image.detect.ImageDetector;

/** @author heaven7 */
public class ImageInitializer {

    private ImageReader imageReader;
    private ImageWriter imageWriter;
    private ImageDetector imageDetector;
    private Matrix2Transformer matrix2Transformer;

    protected ImageInitializer(ImageInitializer.Builder builder) {
        this.imageReader = builder.imageReader;
        this.imageWriter = builder.imageWriter;
        this.imageDetector = builder.imageDetector;
        this.matrix2Transformer = builder.matrix2Transformer;
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

    public static class Builder {
        private ImageReader imageReader;
        private ImageWriter imageWriter;
        private ImageDetector imageDetector;
        private Matrix2Transformer matrix2Transformer;

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

        public ImageInitializer build() {
            return new ImageInitializer(this);
        }
    }
}
