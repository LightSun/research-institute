package com.heaven7.ve.test;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.colorgap.MediaItem;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.ResourceInitializer;
import com.heaven7.ve.colorgap.impl.MediaAnalyserImpl;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * 镜头类型测试
 */
public class ShotTypeTest {

    private static final String TAG = "ShotTypeTest";
    private static final String SRC_DIR = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\男装";

    public static void main(String[] args) {
        ShotTypeTest test = new ShotTypeTest();
        test.testShotType1();

        try {
            System.out.println(URLEncoder.encode("照片", "utf-8"));
            System.out.println(URLEncoder.encode("男装", "utf-8"));
            System.out.println(URLEncoder.encode("丝麻山水长衬衫", "utf-8"));
            // %E7%85%A7%E7%89%87\%E7%94%B7%E8%A3%85\%E4%B8%9D%E9%BA%BB%E5%B1%B1%E6%B0%B4%E9%95%BF%E8%A1%AC%E8%A1%AB
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShotType1() throws RuntimeException{
        final List<BaseMediaResourceItem> items = scanImageItems(FileUtils.ofDirFileFilter("丝麻山水长衬衫"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResourceInitializer.init(null);

                MediaAnalyserImpl analyser = new MediaAnalyserImpl();
                CyclicBarrier barrier = new CyclicBarrier(analyser.getAsyncModuleCount() + 1);
                List<MediaItem> outItems = analyser.analyse(null, items, barrier);

                ConcurrentUtils.awaitBarrier(barrier);
                Logger.d(TAG, "testShotType1", "analyse done...");
                VisitServices.from(outItems).map(new ResultVisitor<MediaItem, MediaPartItem>() {
                    @Override
                    public MediaPartItem visit(MediaItem mediaItem, Object param) {
                        return mediaItem.asPart(new SimpleColorGapContext());
                    }
                }).fire(new FireVisitor<MediaPartItem>() {
                    @Override
                    public Boolean visit(MediaPartItem item, Object param) {
                        System.out.println("file: " + item.getItem().getFilePath() + " ,shotType = " + item.getImageMeta().getShotType());
                        return null;
                    }
                });

            }
        }).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<BaseMediaResourceItem> scanImageItems(FileFilter filter) {
        List<BaseMediaResourceItem> items = new ArrayList<>();
        File file = new File(SRC_DIR);
        List<String> videos = new ArrayList<>();
        FileUtils.getFiles(file, "jpg", filter, videos);
        FileUtils.getFiles(file, "jpeg", filter, videos);
        FileUtils.getFiles(file, "png", filter, videos);

        for (String fileName : videos) {
            Logger.d(TAG, "scanItems", "video file = " + fileName);
            items.add(TestUtils.createImageItem(fileName));
        }
        return items;
    }
}
