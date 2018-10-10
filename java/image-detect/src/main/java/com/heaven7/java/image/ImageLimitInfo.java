package com.heaven7.java.image;

/**
 * the limit info of image
 * @author heaven7
 */
public class ImageLimitInfo {

    private int maxWidth;
    private int maxHeight;

    public ImageLimitInfo(){}

    protected ImageLimitInfo(ImageLimitInfo.Builder builder) {
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public boolean hasLimitWidthOrHeight(){
        return maxWidth > 0 || maxHeight > 0;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public static class Builder {
        private int maxWidth;
        private int maxHeight;

        public Builder setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public ImageLimitInfo build() {
            return new ImageLimitInfo(this);
        }
    }
}
