package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public final class MediaResourceConfiguration {

    private MediaResourceScanner faceScanner;
    private MediaResourceScanner highLightScanner;
    private MediaResourceScanner tagScanner;

    MediaResourceConfiguration(MediaResourceConfiguration.Builder builder) {
        this.faceScanner = builder.faceScanner;
        this.highLightScanner = builder.highLightScanner;
        this.tagScanner = builder.tagScanner;
    }

    public void setFaceScanner(MediaResourceScanner faceScanner) {
        this.faceScanner = faceScanner;
    }

    public void setHighLightScanner(MediaResourceScanner highLightScanner) {
        this.highLightScanner = highLightScanner;
    }

    public void setTagScanner(MediaResourceScanner tagScanner) {
        this.tagScanner = tagScanner;
    }

    public MediaResourceScanner getFaceScanner() {
        return this.faceScanner;
    }

    public MediaResourceScanner getHighLightScanner() {
        return this.highLightScanner;
    }

    public MediaResourceScanner getTagScanner() {
        return this.tagScanner;
    }

    public static class Builder {
        private MediaResourceScanner faceScanner;
        private MediaResourceScanner highLightScanner;
        private MediaResourceScanner tagScanner;

        public Builder setFaceScanner(MediaResourceScanner faceScanner) {
            this.faceScanner = faceScanner;
            return this;
        }

        public Builder setHighLightScanner(MediaResourceScanner highLightScanner) {
            this.highLightScanner = highLightScanner;
            return this;
        }

        public Builder setTagScanner(MediaResourceScanner tagScanner) {
            this.tagScanner = tagScanner;
            return this;
        }

        public MediaResourceConfiguration build() {
            return new MediaResourceConfiguration(this);
        }
    }
}
