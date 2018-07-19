package com.heaven7.test;

import com.heaven7.utils.CommonUtils;

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
    }
}
