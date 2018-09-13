package com.vida.ai.third.baidu;

import com.heaven7.java.image.ImageReader;
import com.heaven7.ve.starter.JavaImageReader;
import com.vida.ai.third.baidu.entity.VBodyAnalysis;
import com.vida.ai.third.baidu.entity.VObjectDetect;
import okhttp3.Call;
import org.junit.Test;

/**
 * @author heaven7
 */
public class KeyPointTest {

    public static void main(String[] args) {
    }

    @Test
    public void testSubject() {
        String imagePath = "F:\\videos\\ClothingWhite\\temp\\LM0A0223\\img_00001.jpg";

        ImageReader.ImageInfo imageInfo = JavaImageReader.DEFAULT.readBytes(imagePath, "jpg");
        VThirdBaiduService service = new VThirdBaiduService();
        service.postObjectDetect(imageInfo.getData(), new VThirdBaiduCallback<VObjectDetect>(service) {
            @Override
            protected void onSuccess(Call call, VObjectDetect obj) {
                System.out.println(obj);
            }
        });
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testKeyPoint() {
        String imagePath = "F:\\videos\\ClothingWhite\\temp\\LM0A0223\\img_00005.jpg";
        //String imagePath = "E:\\tmp\\ChenJun\\test.jpg";

        ImageReader.ImageInfo imageInfo = JavaImageReader.DEFAULT.readBytes(imagePath, "jpg");
        VThirdBaiduService service = new VThirdBaiduService();
        service.postBodyAnalysis(imageInfo.getData(), new VThirdBaiduCallback<VBodyAnalysis>(service) {
            @Override
            protected void onSuccess(Call call, VBodyAnalysis obj) {
                System.out.println(obj);
            }
        });
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
