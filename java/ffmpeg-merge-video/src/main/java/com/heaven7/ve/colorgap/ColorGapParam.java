package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public class ColorGapParam {

    /** the resource data dir often is data dir of batch image. */
    private String resourceDataDir;
    /** the resource data dir often is data dir of batch image. */
    private String batchImageDataDir;

    protected ColorGapParam(ColorGapParam.Builder builder) {
        this.resourceDataDir = builder.resourceDataDir;
        this.batchImageDataDir = builder.batchImageDataDir;
    }

    public String getResourceDataDir() {
        return this.resourceDataDir;
    }

    public String getBatchImageDataDir() {
        return this.batchImageDataDir;
    }

    public static class Builder {
        /** the resource data dir often is data dir of batch image. */
        private String resourceDataDir;
        /** the resource data dir often is data dir of batch image. */
        private String batchImageDataDir;

        public Builder setResourceDataDir(String resourceDataDir) {
            this.resourceDataDir = resourceDataDir;
            return this;
        }

        public Builder setBatchImageDataDir(String batchImageDataDir) {
            this.batchImageDataDir = batchImageDataDir;
            return this;
        }

        public ColorGapParam build() {
            return new ColorGapParam(this);
        }
    }
}
