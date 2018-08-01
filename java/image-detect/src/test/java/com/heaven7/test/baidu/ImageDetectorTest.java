package com.heaven7.test.baidu;

import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.detect.BatchInfo;
import com.heaven7.java.image.detect.ImageDetector;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.test.JavaImageReader;
import org.junit.Test;

import java.util.List;

/**
 * @author heaven7
 */
public class ImageDetectorTest {

    final BaiduImageDetector detector = new BaiduImageDetector();
    final JavaImageReader mReader = new JavaImageReader();

    @Test
    public void test1() {
        //test_4 is a merged image
        ImageReader.ImageInfo info = mReader.readBytes("E:\\tmp\\upload_files\\test_4.jpg", "jpg");
        detector.detectKeyPointsBatch(
                BatchInfo.of(4, info.getWidth() / 2, info.getHeight() / 2),
                info.getData(), new ImageDetector.OnDetectCallback<List<KeyPointData>>() {
                    @Override
                    public void onFailed(int code, String msg) {
                        System.out.println(msg);
                    }

                    @Override
                    public void onSuccess(List<KeyPointData> data) {

                    }

                    @Override
                    public void onBatchSuccess(SparseArray<List<KeyPointData>> batchData) {
                        System.out.println(batchData.size());
                    }
                });

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
