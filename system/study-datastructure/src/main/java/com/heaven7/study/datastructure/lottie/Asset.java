package com.heaven7.study.datastructure.lottie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author heaven7
 */
public class Asset {

    private String id;

    private List<Layer> layers; //precompile layers
    @SerializedName("w")
    private int width;
    @SerializedName("h")
    private int height;

    @SerializedName("p")
    private String imageFileName; //simple name.
    @SerializedName("u")
    private String relativeFolder;


}
