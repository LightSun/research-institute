package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public class ColorGapParam {

    /** the resource dir . often used for pre load batch image data. */
    private String resourceDir;
    /** the resource data dir often is data dir of batch image. */
    private String resourceDataDir;

    protected ColorGapParam(ColorGapParam.Builder builder) {
        this.resourceDir = builder.resourceDir;
        this.resourceDataDir = builder.resourceDataDir;
    }

    public String getResourceDir() {
        return this.resourceDir;
    }

    public String getResourceDataDir() {
        return this.resourceDataDir;
    }

    public static class Builder {
        /** the resource dir . often used for pre load batch image data. */
        private String resourceDir;
        private String resourceDataDir;

        public Builder setResourceDir(String resourceDir) {
            this.resourceDir = resourceDir;
            return this;
        }

        public Builder setResourceDataDir(String resourceDataDir) {
            this.resourceDataDir = resourceDataDir;
            return this;
        }

        public ColorGapParam build() {
            return new ColorGapParam(this);
        }
    }
}
