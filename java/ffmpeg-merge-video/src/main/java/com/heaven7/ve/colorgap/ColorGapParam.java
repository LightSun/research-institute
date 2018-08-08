package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public class ColorGapParam {

    /** the resource dir . often used for pre load batch image data. */
    private String resourceDir;

    protected ColorGapParam(ColorGapParam.Builder builder) {
        this.resourceDir = builder.resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        this.resourceDir = resourceDir;
    }

    public String getResourceDir() {
        return this.resourceDir;
    }

    public static class Builder {
        private String resourceDir;

        public Builder setResourceDir(String resourceDir) {
            this.resourceDir = resourceDir;
            return this;
        }
        public ColorGapParam build() {
            return new ColorGapParam(this);
        }
    }
}
