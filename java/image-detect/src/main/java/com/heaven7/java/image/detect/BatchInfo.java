package com.heaven7.java.image.detect;

/**
 * @author heaven7
 */
public class BatchInfo {

    private int count;
    private int width; //for every image
    private int height;

    protected BatchInfo(BatchInfo.Builder builder) {
        this.count = builder.count;
        this.width = builder.width;
        this.height = builder.height;
    }

    public static BatchInfo of(int count, int width, int height){
        return new Builder()
                .setCount(count)
                .setWidth(width)
                .setHeight(height)
                .build();
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCount() {
        return this.count;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public static class Builder {
        private int count;
        private int width; //for every image
        private int height;

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public BatchInfo build() {
            return new BatchInfo(this);
        }
    }
}
