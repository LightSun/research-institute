package com.heaven7.test;

import com.heaven7.test_okh.LogCallback;
import com.heaven7.test_okh.OkHttpHelper;
import com.heaven7.utils.CommonUtils;

import java.util.HashMap;

public class BaseTest {

    public void waitDone(){
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String str = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\浅蓝围巾领开衫";
        System.out.println(CommonUtils.urlEncode(str));
        //阿姆达而法则
        System.out.println((1f / ((1-0.2f)+ 0.2f/6)));
        System.out.println((1f/(1-0.6f+0.6/1.5)));


        int sampleSize = findBestSampleSize(1200, 1600, 224, 224);
        System.out.println(sampleSize);
    }

    static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
}
