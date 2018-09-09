package com.vida.ai.data.transform;

/**
 * Created by heaven7 on 2018/9/9.
 */
public class DataWrapper {

    private String filename;
    private IosHighLightData data;

    public DataWrapper(String filename, IosHighLightData data) {
        this.filename = filename;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }
    public IosHighLightData getData() {
        return data;
    }
}
