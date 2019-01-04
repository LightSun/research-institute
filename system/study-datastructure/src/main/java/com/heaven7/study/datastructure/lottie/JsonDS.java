package com.heaven7.study.datastructure.lottie;

import com.google.gson.annotations.SerializedName;
import com.heaven7.java.base.util.SparseArray;

import java.util.List;

/**
 * @author heaven7
 */
public class JsonDS {

    @SerializedName("w")
    private int width;
    @SerializedName("h")
    private int height;
    @SerializedName("fr")
    private int frameRate;

    @SerializedName("ip")
    private float startFrame;

    @SerializedName("op")
    private float endFrame;

    @SerializedName("v")
    private String version;

    private List<Asset> assets;
    private List<Layer> layers;
    private List<Font> fonts;
    private SparseArray<FontCharacter> chars;

}
