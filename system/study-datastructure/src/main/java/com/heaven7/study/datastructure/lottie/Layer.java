package com.heaven7.study.datastructure.lottie;

import com.google.gson.annotations.SerializedName;
import com.heaven7.study.datastructure.lottie.mocks.ContentModel;
import com.heaven7.study.datastructure.lottie.mocks.Transform;

import java.util.List;

/**
 * 图形选择，长按
 * 在AE里面： 一个图形有原点，  和 图形(图形相对于原点是不动的)。
 *           描点(a)是图形相对于原点在动，
 *           位置(p)是原点在动。
 * @author heaven7
 */
public class Layer {

    public static final int TYPE_GR = 1;  //ShapeGroupParser
    public static final int TYPE_ST = 2;  //ShapeStrokeParser
    public static final int TYPE_GS = 3;  //GradientStrokeParser
    public static final int TYPE_FL = 4;  //ShapeFillParser
    public static final int TYPE_GF = 5;  //GradientFillParser
    public static final int TYPE_TR = 6;  //AnimatableTransformParser

    public static final int TYPE_SH = 7;  //ShapePathParser
    public static final int TYPE_EL = 8;  //CircleShapeParser
    public static final int TYPE_RC = 9;  //RectangleShapeParser
    public static final int TYPE_TM = 10;  //ShapeTrimPathParser
    public static final int TYPE_SR = 11;  //PolystarShapeParser

    public static final int TYPE_MM = 12;  //MergePathsParser
    public static final int TYPE_RP = 13;  //RepeaterParser

    @SerializedName("nm")
    private String name;   //layer name
    @SerializedName("ind")  //layer id
    private int id;

    @SerializedName("refId")
    private String referId;

    @SerializedName("ty")
    private int type; //
    @SerializedName("parent")
    private int parentId;

    @SerializedName("sw")
    private int solidWidth;
    @SerializedName("sh")
    private int solidHeight;
    @SerializedName("sc")
    private String solidColor;

    @SerializedName("tt")
    private int matterType;

    @SerializedName("ks")
    private Transform transform;


    @SerializedName("sr")
    private float timeStretch; //时间伸展
    @SerializedName("st")
    private float startFrame;
    @SerializedName("w")
    private int preCompileWidth;
    @SerializedName("h")
    private int preCompileHeight;
    @SerializedName("ip")
    private float inFrame;
    @SerializedName("op")
    private float outFrame;
    @SerializedName("tm")
    private float timeRemapping;

    private List<ContentModel> shapes;
}
