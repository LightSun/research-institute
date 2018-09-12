package com.vida.ai.third.baidu;

import com.heaven7.java.image.ImageReader;
import com.heaven7.ve.starter.JavaImageReader;
import com.vida.ai.third.baidu.entity.VBodyAnalysis;
import okhttp3.Call;

/**
 * @author heaven7
 */
public class KeyPointTest {

    public static void main(String[] args) {
        String imagePath = "F:\\videos\\ClothingWhite\\temp\\LM0A0212\\img_00004.jpg";
        //String imagePath = "E:\\tmp\\ChenJun\\test.jpg";

        ImageReader.ImageInfo imageInfo = JavaImageReader.DEFAULT.readBytes(imagePath, "jpg");
        VThirdBaiduService service = new VThirdBaiduService();
        service.postBodyAnalysis(imageInfo.getData(), new VThirdBaiduCallback<VBodyAnalysis>(service) {
            @Override
            protected void onSuccess(Call call, VBodyAnalysis obj) {
                System.out.println(obj);
            }
        });
    }
}
