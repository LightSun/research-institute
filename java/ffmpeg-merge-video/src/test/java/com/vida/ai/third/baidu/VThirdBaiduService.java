package com.vida.ai.third.baidu;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.heaven7.java.base.util.threadpool.Executors2;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.vida.ai.third.baidu.entity.VBodyAnalysis;
import com.vida.ai.third.baidu.entity.VDetectionVidaSKI;
import com.vida.ai.third.baidu.entity.VObjectDetect;
import com.vida.ai.third.baidu.entity.VThirdBaiduAccessToken;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class VThirdBaiduService implements VThirdBaiduCallback.RequestService{

    private static VThirdBaiduAccessToken accessToken;
    private ExecutorService mService = Executors2.newFixedThreadPool(5);

    public static void main(String[] args) {

        //https://oauth.jd.com/oauth/token?grant_type=authorization_code&client_id=&redirect_uri=http://192.168.3.142&code=GET_CODE&state=1&client_secret=
        String image = "E:\\tmp\\upload_files\\test_4.jpg";

        VThirdBaiduService service = new VThirdBaiduService();
        service.postDetectionVidaSKI(image, new VThirdBaiduCallback<VDetectionVidaSKI>(service) {
            @Override
            protected void onSuccess(Call call, VDetectionVidaSKI obj) {
                System.out.println(obj.toString());
            }
        });
    }
    /**
     * 获取百度token
     * @param ak
     * @param sk
     */
    public void postAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;

        Map<String, String> param = new HashMap<>();
        try {
            Response response = OkHttpHelper.postSync(getAccessTokenUrl, RequestBodys.toFormUrlEncodeBody(param));
            ResponseBody body = response.body();
            String responseBodyString = body.string();
            Gson gson = new GsonBuilder().create();
            accessToken = gson.fromJson(responseBodyString, VThirdBaiduAccessToken.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片主体检测
     * @param imageBytes
     * @param callback
     */
    public void postObjectDetect(byte[] imageBytes, VThirdBaiduCallback<VObjectDetect> callback) {
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/object_detect";
        String requestUrl = urlAddToken(url);

        String imageString = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, String> param = new HashMap<>();
        param.put("image", imageString);
        param.put("with_face", "1");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");

        post(requestUrl, header, RequestBodys.toFormUrlEncodeBody(param), callback);
    }

    public void postObjectDetect(String image, VThirdBaiduCallback<VObjectDetect> callback) {
        File imageFile = new File(image);
        String fileType = FileUtils.getFileExtension(imageFile);
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bufferedImage = ImageIO.read(imageFile);
            ImageIO.write(bufferedImage, fileType, bos);
            byte[] imageBytes = bos.toByteArray();
            postObjectDetect(imageBytes, callback);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 人体检测接口
     * @param imageBytes
     * @param callback
     */
    public void postBodyAnalysis(byte[] imageBytes, VThirdBaiduCallback<VBodyAnalysis> callback) {
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_analysis";
        String requestUrl = urlAddToken(url);

        String imageString = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, String> param = new HashMap<>();
        param.put("image", imageString);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");

        post(requestUrl, header, RequestBodys.toFormUrlEncodeBody(param), callback);
    }

    public void postBodyAnalysis(String image, VThirdBaiduCallback<VBodyAnalysis> callback) {
        File imageFile = new File(image);
        String fileType = FileUtils.getFileExtension(imageFile);
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bufferedImage = ImageIO.read(imageFile);
            ImageIO.write(bufferedImage, fileType, bos);
            byte[] imageBytes = bos.toByteArray();
            postBodyAnalysis(imageBytes, callback);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子模型接口，获取滑雪高光
     * @param imageBytes
     * @param callback
     */
    public void postDetectionVidaSKI(byte[] imageBytes, VThirdBaiduCallback<VDetectionVidaSKI> callback) {
        //String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/detection/Vida_SKI";
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/detection/clothing"; //服装
        String requestUrl = urlAddToken(url);

        String imageString = Base64.getEncoder().encodeToString(imageBytes);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image", imageString);
        jsonObject.addProperty("threshold", "0.4");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        post(requestUrl, header, requestBody, callback);
    }

    public void postDetectionVidaSKI(String image, VThirdBaiduCallback<VDetectionVidaSKI> callback) {
        File imageFile = new File(image);
        String fileType = FileUtils.getFileExtension(imageFile);
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bufferedImage = ImageIO.read(imageFile);
            ImageIO.write(bufferedImage, fileType, bos);
            byte[] imageBytes = bos.toByteArray();
            postDetectionVidaSKI(imageBytes, callback);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * url请求加上token
     * @param baseUrl
     * @return
     */
    private String urlAddToken(String baseUrl) {
        if (accessToken == null) {
            postAuth("6dD8acznGrtKRIwkVOeCeaKZ", "vNQIpqoFPHIU8G1WjeSO9EOOgUs1dzNj");
        }
        String requestUrl = baseUrl
                + "?access_token=" + accessToken.getAccess_token();
        return requestUrl;
    }

    /**
     * 更新token
     */
    private void updateToken() {
        postAuth("6dD8acznGrtKRIwkVOeCeaKZ", "vNQIpqoFPHIU8G1WjeSO9EOOgUs1dzNj");
    }

    @Override
    protected void finalize() throws Throwable {
        if(mService != null){
            mService.shutdownNow();
            mService = null;
        }
        super.finalize();
    }

    /** for limit request count of bai-du-http */
    private void post(String url, Map<String, String> headers, RequestBody body, Callback callback){
        Request.Builder builder = new Request.Builder().url(url)
                .post(body);
        if(!headers.isEmpty()) {
            VisitServices.from(headers).fire(new MapFireVisitor<String, String>() {
                @Override
                public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                    builder.addHeader(pair.getKey(), pair.getValue());
                    return null;
                }
            });
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(mService))
                .build();
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(callback);
    }

    public void postRequest(Request request, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(mService))
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
