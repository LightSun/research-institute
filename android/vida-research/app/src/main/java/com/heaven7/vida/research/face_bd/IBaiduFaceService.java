package com.heaven7.vida.research.face_bd;

import com.heaven7.vida.research.retrofit.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by heaven7 on 2018/4/27 0027.
 */

public interface IBaiduFaceService {


    @POST//("rest/2.0/face/v2/detect") // ?access_token=xxx
    @FormUrlEncoded //V3
    Call<Result<ArrayList<FaceInfo>>> getFaceInfo(@Url String url, @Field("image")String base64Image,
                                                 @Field("image_type")String image_type,
                                                  @Field("max_face_num") int max_face_num,   //option
                                                  @Field("face_fields") String face_fields); //option

    @POST//("rest/2.0/face/v2/detect") // ?access_token=xxx
    @FormUrlEncoded
    Call<Result<ArrayList<FaceInfo>>> getFaceInfo(@Url String url, @Field("image")String base64Image,
                                                  @Field("max_face_num") int max_face_num,   //option
                                                  @Field("face_fields") String face_fields); //option

    @POST
    @FormUrlEncoded
    Call<Result<MainClassifyBean>> getMainClassifyResult(@Url String url,
                                                  @Field("image")String base64Image,
                                                  @Field("with_face")int with_face);



}
/*
 * image	是	string	base64编码后的图片数据，需urlencode，编码后的图片大小不超过2M
 max_face_num	否	uint32	最多处理人脸的数目，默认值为1，仅检测图片中面积最大的那个人脸
 face_fields	否	string	包括age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities信息，逗号分隔，默认只返回人脸框、概率和旋转角度
String param = "max_face_num=" + 5 + "&face_fields=" + "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities" + "&image=" + imgParam;
*/