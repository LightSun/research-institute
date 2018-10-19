package com.heaven7.ve.colorgap;

import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.Location;

import java.util.List;

/**
 * @author heaven7
 */
public final class MediaResourceConfiguration {

    private MediaResourceScanner faceScanner;
    private MediaResourceScanner highLightScanner;
    private MediaResourceScanner tagScanner;
    private FrameDataDelegate<List<KeyPointData>> keyPointDelegate;
    private FrameDataDelegate<Location> subjectDelegate;

    MediaResourceConfiguration(MediaResourceConfiguration.Builder builder) {
        this.faceScanner = builder.faceScanner;
        this.highLightScanner = builder.highLightScanner;
        this.tagScanner = builder.tagScanner;
        this.keyPointDelegate = builder.keyPointDelegate;
        this.subjectDelegate = builder.subjectDelegate;
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

    public FrameDataDelegate<List<KeyPointData>> getKeyPointDelegate() {
        return this.keyPointDelegate;
    }

    public FrameDataDelegate<Location> getSubjectDelegate() {
        return this.subjectDelegate;
    }

    public static class Builder {
        private MediaResourceScanner faceScanner;
        private MediaResourceScanner highLightScanner;
        private MediaResourceScanner tagScanner;
        private FrameDataDelegate<List<KeyPointData>> keyPointDelegate;
        private FrameDataDelegate<Location> subjectDelegate;

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

        public Builder setKeyPointDelegate(FrameDataDelegate<List<KeyPointData>> keyPointDelegate) {
            this.keyPointDelegate = keyPointDelegate;
            return this;
        }

        public Builder setSubjectDelegate(FrameDataDelegate<Location> subjectDelegate) {
            this.subjectDelegate = subjectDelegate;
            return this;
        }

        public MediaResourceConfiguration build() {
            return new MediaResourceConfiguration(this);
        }
    }
}
