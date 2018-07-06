package com.heaven7.vida.research.face_bd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.VidaTestApplication;
import com.heaven7.vida.research.retrofit.AbstractRetrofitCallback;
import com.heaven7.vida.research.retrofit.Result;
import com.heaven7.vida.research.retrofit.RetrofitCaller;
import com.heaven7.vida.research.retrofit.RetrofitHelper;
import com.heaven7.vida.research.utils.DrawableUtils2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

/**
 * Created by heaven7 on 2018/4/27 0027.
 */

public class FaceRequest {

    private static final String TAG = "FaceRequest";
    private static final String URL2 = "https://aip.baidubce.com/rest/2.0/face/v2/detect";
    private static final String URL3 = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
     //主体内容识别
    private static final String URL_MAIN_CLASSIFY = "https://aip.baidubce.com/rest/2.0/image-classify/v1/object_detect";
    //人体关键点识别
    private static final String URL_BODY_ANALYSE = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_analysis";
    //人体属性点
    private static final String URL_BODY_ATTR = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_attr";
    private static final String FACE_FIELDS = "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities";
    private ExecutorService mService = Executors.newCachedThreadPool();

    /** 人体属性点分析 */
    public void getBodyAttr(Context context, final @DrawableRes int imageId,
                               final AbstractRetrofitCallback<BodyAttrData> callback){
        if (mService == null) {
            mService = Executors.newCachedThreadPool();
        }
        final Context appContext = context.getApplicationContext();
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), imageId);
                final String imgStr = Base64Util.encode(DrawableUtils2.bitmap2Bytes(bitmap));
                final String url = URL_BODY_ATTR + "?access_token=" + AuthService.getClassifyToken2();
                RetrofitCaller<IBaiduFaceService, BodyAttrData> caller = RetrofitCaller.create(IBaiduFaceService.class);
                caller.callFactory(new RetrofitCaller.CallFactory<IBaiduFaceService, BodyAttrData>() {
                    @Override
                    public Call<Result<BodyAttrData>> create(Class<IBaiduFaceService> clazz, String baseUrl) {
                        return RetrofitHelper.build(VidaTestApplication.URL_FACE,clazz).getBodyAttr(url, imgStr);
                    }
                })
                        .callback(callback)
                        .call();
            }
        });
    }
    /** 人体关键点分析 */
    public void getBodyAnalyse(Context context, final @DrawableRes int imageId,
                               final AbstractRetrofitCallback<BodyData> callback){
        if (mService == null) {
            mService = Executors.newCachedThreadPool();
        }
        final Context appContext = context.getApplicationContext();
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), imageId);
                final String imgStr = Base64Util.encode(DrawableUtils2.bitmap2Bytes(bitmap));
                final String url = URL_BODY_ANALYSE + "?access_token=" + AuthService.getClassifyToken2();
                RetrofitCaller<IBaiduFaceService, BodyData> caller = RetrofitCaller.create(IBaiduFaceService.class);
                caller.callFactory(new RetrofitCaller.CallFactory<IBaiduFaceService, BodyData>() {
                    @Override
                    public Call<Result<BodyData>> create(Class<IBaiduFaceService> clazz, String baseUrl) {
                        return RetrofitHelper.build(VidaTestApplication.URL_FACE,clazz).getBodyAnalyse(url, imgStr);
                    }
                })
                .callback(callback)
                .call();
            }
        });
    }

    /** 人脸检测2 */
    public void startRequest2(Context context, final @DrawableRes int imageId) {
        if (mService == null) {
            mService = Executors.newCachedThreadPool();
        }
        final Context appContext = context.getApplicationContext();
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), imageId);
                final String imgStr = Base64Util.encode(DrawableUtils2.bitmap2Bytes(bitmap));
                String imgParam = null;
                try {
                    imgParam = URLEncoder.encode(imgStr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String param = "max_face_num=" + 5 + "&face_fields=" + "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities"
                        + "&image=" + imgParam;

                try {
                    String result = HttpUtil.post(URL2, AuthService.getToken(), param);
                    Logger.d(TAG, "startRequest2", "result = " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /** 主体内容识别 */
    public void startMainClassify(Context context, final @DrawableRes int imageId ,
                                  final AbstractRetrofitCallback<MainClassifyBean> callback){
        if (mService == null) {
            mService = Executors.newCachedThreadPool();
        }
        final Context appContext = context.getApplicationContext();
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), imageId);
                final String imgStr = Base64Util.encode(DrawableUtils2.bitmap2Bytes(bitmap));
                final String url = URL_MAIN_CLASSIFY + "?access_token=" + AuthService.getClassifyToken();

                RetrofitCaller<IBaiduFaceService, MainClassifyBean> caller = RetrofitCaller.create(IBaiduFaceService.class);
                caller.callFactory(new RetrofitCaller.CallFactory<IBaiduFaceService, MainClassifyBean>() {
                    @Override
                    public Call<Result<MainClassifyBean>> create(Class<IBaiduFaceService> clazz, String baseUrl) {

                        return RetrofitHelper.build(VidaTestApplication.URL_FACE, clazz)
                                .getMainClassifyResult(url, imgStr, 1);
                    }
                }).callback(callback).call();
            }
        });
    }

    /** 人脸检测 */
    public void startRequest(Context context, final @DrawableRes int imageId,
                             final AbstractRetrofitCallback<ArrayList<FaceInfo>> callback) {
        if (mService == null) {
            mService = Executors.newCachedThreadPool();
        }
        final Context appContext = context.getApplicationContext();
        mService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), imageId);
               /* ByteArrayInputStream input = new ByteArrayInputStream(BitmapBase64Utils.bitmap2Bytes(bitmap));
                int exifOrientation = getExifOrientation(input);
                Logger.d(TAG, "run", "exifOrientation = " + exifOrientation);*/

                final String imgStr = Base64Util.encode(DrawableUtils2.bitmap2Bytes(bitmap));

                //final String base64Image = BitmapBase64Utils.bitmap2String2(bitmap); //have bugs
                final String url = URL2 + "?access_token=" + AuthService.getToken();

                RetrofitCaller<IBaiduFaceService, ArrayList<FaceInfo>> caller = RetrofitCaller.create(IBaiduFaceService.class);
                caller.callFactory(new RetrofitCaller.CallFactory<IBaiduFaceService, ArrayList<FaceInfo>>() {
                    @Override
                    public Call<Result<ArrayList<FaceInfo>>> create(Class<IBaiduFaceService> clazz, String baseUrl) {
                        return RetrofitHelper.build(VidaTestApplication.URL_FACE, clazz)
                                .getFaceInfo(url, imgStr, 5, FACE_FIELDS);
                    }
                }).callListener(new RetrofitCaller.CallListener() {
                    @Override
                    public void onStart() {
                        Logger.d(TAG, "onStart", "");
                    }

                    @Override
                    public void beforeResponse() {
                        Logger.d(TAG, "beforeResponse", "");
                    }

                    @Override
                    public void onEnd(int code) {
                        Logger.d(TAG, "onEnd", RetrofitCaller.codeToString(code));
                    }
                }).callback(callback).call();
            }
        });
    }

    public void destroy() {
        if (mService != null) {
            mService.shutdownNow();
            mService = null;
        }
    }

    public static String getPathFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return path;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getExifOrientation(InputStream in) {

        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
// We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;

                        break;
                    default:
                        break;
                }
            }
        }
        return degree;
    }
}
