package com.vida.common;

/**
 * the cmds of image extract from video
 *
 * @author heaven7
 */
public class ImageExtractCmd {
    /**
     * -i,  MUST
     */
    private String videoPath;
    /**
     * extract image rate in every second(-r) . MUST
     */
    private int countEverySecond = 1;
    /**
     * the image save format(-f)
     */
    private String imageFormat;

    /**
     * the start time (in seconds) to extract(-ss)
     */
    private float startTime;
    /**
     * the extract count.(-vframes)
     */
    private int frameCount;

    /**
     * like '-s 800*600'
     */
    private String resolution;

    /**
     * can be a jpeg file path or just a dir. MUST
     */
    private String savePath;

    private boolean jpg;

    protected ImageExtractCmd(ImageExtractCmd.Builder builder) {
        this.videoPath = builder.videoPath;
        this.countEverySecond = builder.countEverySecond;
        this.imageFormat = builder.imageFormat;
        this.startTime = builder.startTime;
        this.frameCount = builder.frameCount;
        this.resolution = builder.resolution;
        this.savePath = builder.savePath;
        this.jpg = builder.jpg;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setCountEverySecond(int countEverySecond) {
        this.countEverySecond = countEverySecond;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    /**
     * the start time (in seconds)
     */
    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public int getCountEverySecond() {
        return this.countEverySecond;
    }

    public String getImageFormat() {
        return this.imageFormat;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public String getResolution() {
        return this.resolution;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public boolean isJpg() {
        return this.jpg;
    }

    public static class Builder {
        /**
         * -i,  MUST
         */
        private String videoPath;
        /**
         * extract image rate in every second(-r) . MUST
         */
        private int countEverySecond = 1;
        /**
         * the image save format(-f)
         */
        private String imageFormat;
        /**
         * the start time (in seconds) to extract(-ss)
         */
        private float startTime;
        /**
         * the extract count.(-vframes)
         */
        private int frameCount;
        /**
         * like '-s 800*600'
         */
        private String resolution;
        /**
         * can be a jpeg file path or just a dir. MUST
         */
        private String savePath;
        private boolean jpg;

        public Builder setVideoPath(String videoPath) {
            this.videoPath = videoPath;
            return this;
        }

        public Builder setCountEverySecond(int countEverySecond) {
            this.countEverySecond = countEverySecond;
            return this;
        }

        public Builder setImageFormat(String imageFormat) {
            this.imageFormat = imageFormat;
            return this;
        }

        public Builder setStartTime(float startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setFrameCount(int frameCount) {
            this.frameCount = frameCount;
            return this;
        }

        public Builder setResolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder setJpg(boolean jpg) {
            this.jpg = jpg;
            return this;
        }

        public ImageExtractCmd build() {
            return new ImageExtractCmd(this);
        }
    }
}